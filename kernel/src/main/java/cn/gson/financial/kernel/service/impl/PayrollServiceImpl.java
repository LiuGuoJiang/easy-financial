package cn.gson.financial.kernel.service.impl;

import cn.gson.financial.kernel.common.DateUtil;
import cn.gson.financial.kernel.exception.ServiceException;
import cn.gson.financial.kernel.model.entity.*;
import cn.gson.financial.kernel.model.mapper.*;
import cn.gson.financial.kernel.model.vo.UserVo;
import cn.gson.financial.kernel.service.PayrollService;
import cn.gson.financial.kernel.service.VoucherService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@AllArgsConstructor
public class PayrollServiceImpl implements PayrollService {
    private EmployeeMapper employeeMapper;
    private PayrollItemMapper payrollItemMapper;
    private PayrollPeriodMapper payrollPeriodMapper;
    private PayrollSheetMapper payrollSheetMapper;
    private PayrollDetailMapper payrollDetailMapper;
    private PayrollConfigMapper payrollConfigMapper;
    private SubjectMapper subjectMapper;
    private VoucherService voucherService;

    private static final Pattern TOKEN = Pattern.compile("[A-Za-z_][A-Za-z0-9_]*");

    @Override
    @Transactional
    public void initDefaultItems(Integer accountSetsId) {
        if (!items(accountSetsId).isEmpty()) return;
        saveItem(accountSetsId, "BASE_SALARY", "基本工资", "EARNING", "BASE_SALARY", 10, true);
        saveItem(accountSetsId, "ALLOWANCE", "补贴", "EARNING", "0", 20, false);
        saveItem(accountSetsId, "SOCIAL_SECURITY", "社保", "DEDUCTION", "BASE_SALARY*0.08", 30, true);
        saveItem(accountSetsId, "HOUSING_FUND", "公积金", "DEDUCTION", "BASE_SALARY*0.07", 40, true);
        saveItem(accountSetsId, "IIT", "个税", "max((GROSS-SOCIAL_SECURITY-HOUSING_FUND-5000)*0.03,0)", 50, true);
        saveItem(accountSetsId, "OTHER_DEDUCTION", "其他扣款", "DEDUCTION", "0", 60, false);
        saveItem(accountSetsId, "NET_PAY", "实发工资", "NET", "GROSS-DEDUCTION", 90, true);
    }

    @Override
    @Transactional
    public List<Map<String, Object>> calculate(Integer accountSetsId, Integer year, Integer month) {
        initDefaultItems(accountSetsId);
        PayrollPeriod period = period(accountSetsId, year, month);
        PayrollSheet sheet = sheet(accountSetsId, period);
        if ("AUDITED".equals(sheet.getStatus())) throw new ServiceException("工资表已审核，不能重新计算！");
        deleteDetails(sheet.getId(), accountSetsId);
        List<Employee> employees = employees(accountSetsId);
        List<PayrollItem> payrollItems = items(accountSetsId);
        List<PayrollDetail> allDetails = new ArrayList<>();
        for (Employee employee : employees) {
            Map<String, Double> values = configValues(accountSetsId);
            values.put("BASE_SALARY", value(employee.getBaseSalary()));
            double gross = 0d;
            double deduction = 0d;
            for (PayrollItem item : payrollItems) {
                double amount;
                if ("NET".equals(item.getItemType())) continue;
                amount = calculateItem(item, values);
                values.put(item.getItemCode(), amount);
                if ("DEDUCTION".equals(item.getItemType())) deduction += amount; else gross += amount;
                allDetails.add(detail(sheet, employee, item, amount));
            }
            values.put("GROSS", round(gross));
            values.put("DEDUCTION", round(deduction));
            for (PayrollItem item : payrollItems) {
                if (!"NET".equals(item.getItemType())) continue;
                double amount = calculateItem(item, values);
                values.put(item.getItemCode(), amount);
                allDetails.add(detail(sheet, employee, item, amount));
            }
        }
        allDetails.forEach(payrollDetailMapper::insert);
        refreshSheetAmount(sheet);
        return payrollMatrix(sheet.getId(), accountSetsId);
    }

    @Override
    @Transactional
    public PayrollSheet audit(Integer accountSetsId, Integer sheetId, UserVo user) {
        PayrollSheet sheet = loadSheet(accountSetsId, sheetId);
        if ("AUDITED".equals(sheet.getStatus())) return sheet;
        if (detailCount(sheetId, accountSetsId) == 0) throw new ServiceException("工资表没有明细，请先计算工资！");
        sheet.setStatus("AUDITED");
        sheet.setAuditDate(new Date());
        sheet.setAuditMemberId(user == null ? null : user.getId());
        sheet.setAuditMemberName(user == null ? null : user.getRealName());
        payrollSheetMapper.updateById(sheet);
        return sheet;
    }

    @Override
    @Transactional
    public PayrollSheet cancelAudit(Integer accountSetsId, Integer sheetId) {
        PayrollSheet sheet = loadSheet(accountSetsId, sheetId);
        if (sheet.getVoucherId() != null) throw new ServiceException("工资表已生成凭证，不能反审核！");
        sheet.setStatus("DRAFT");
        sheet.setAuditDate(null);
        sheet.setAuditMemberId(null);
        sheet.setAuditMemberName(null);
        payrollSheetMapper.updateById(sheet);
        return sheet;
    }

    @Override
    @Transactional
    public Integer generateVoucher(Integer accountSetsId, Integer sheetId, UserVo user) {
        PayrollSheet sheet = loadSheet(accountSetsId, sheetId);
        if (!"AUDITED".equals(sheet.getStatus())) throw new ServiceException("工资表审核后才能生成凭证！");
        if (sheet.getVoucherId() != null) return sheet.getVoucherId();
        List<PayrollDetail> details = detailList(sheetId, accountSetsId);
        if (details.isEmpty()) throw new ServiceException("工资表没有明细！");
        Voucher voucher = baseVoucher(accountSetsId, sheet, user);
        List<VoucherDetails> voucherDetails = new ArrayList<>();
        addExpenseDetails(voucherDetails, details);
        addCreditDetails(voucherDetails, details);
        voucher.setDetails(voucherDetails);
        voucherService.save(voucher);
        sheet.setVoucherId(voucher.getId());
        payrollSheetMapper.updateById(sheet);
        return voucher.getId();
    }

    private void addExpenseDetails(List<VoucherDetails> voucherDetails, List<PayrollDetail> details) {
        Map<String, Double> map = new LinkedHashMap<>();
        Map<String, PayrollDetail> sample = new HashMap<>();
        for (PayrollDetail detail : details) {
            if (detail.getExpenseSubjectId() == null || detail.getAmount() == null || detail.getAmount() == 0d) continue;
            if ("DEDUCTION".equals(detail.getItemType()) || "NET".equals(detail.getItemType())) continue;
            String key = detail.getExpenseSubjectId() + "|" + detail.getDepartmentName() + "|" + detail.getEmployeeName();
            map.put(key, value(map.get(key)) + detail.getAmount());
            sample.put(key, detail);
        }
        for (Map.Entry<String, Double> entry : map.entrySet()) {
            PayrollDetail d = sample.get(entry.getKey());
            voucherDetails.add(voucherDetail(subject(d.getExpenseSubjectId(), "工资费用科目"), "计提工资：" + d.getEmployeeName(), round(entry.getValue()), null, d));
        }
    }

    private void addCreditDetails(List<VoucherDetails> voucherDetails, List<PayrollDetail> details) {
        Map<String, Double> amountBySubject = new LinkedHashMap<>();
        Map<String, PayrollDetail> sample = new HashMap<>();
        for (PayrollDetail detail : details) {
            if (!"DEDUCTION".equals(detail.getItemType()) && !"NET".equals(detail.getItemType())) continue;
            PayrollItem item = itemByCode(detail.getAccountSetsId(), detail.getItemCode());
            Integer subjectId = item == null ? null : item.getPayableSubjectId();
            if (subjectId == null) throw new ServiceException(detail.getItemName() + "未维护应付/代扣科目！");
            String key = String.valueOf(subjectId);
            amountBySubject.put(key, value(amountBySubject.get(key)) + value(detail.getAmount()));
            sample.put(key, detail);
        }
        for (Map.Entry<String, Double> entry : amountBySubject.entrySet()) {
            PayrollDetail d = sample.get(entry.getKey());
            voucherDetails.add(voucherDetail(subject(Integer.parseInt(entry.getKey()), "工资贷方科目"), "计提工资：" + d.getItemName(), null, round(entry.getValue()), d));
        }
    }

    private PayrollItem itemByCode(Integer accountSetsId, String code) {
        LambdaQueryWrapper<PayrollItem> qw = Wrappers.lambdaQuery();
        qw.eq(PayrollItem::getAccountSetsId, accountSetsId).eq(PayrollItem::getItemCode, code);
        return payrollItemMapper.selectOne(qw);
    }

    private Voucher baseVoucher(Integer accountSetsId, PayrollSheet sheet, UserVo user) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, sheet.getPayrollYear());
        calendar.set(Calendar.MONTH, sheet.getPayrollMonth() - 1);
        Date date = DateUtil.getMonthEnd(calendar.getTime());
        Voucher voucher = new Voucher();
        voucher.setWord("记");
        voucher.setCode(voucherService.loadCode(accountSetsId, "记", date));
        voucher.setRemark(sheet.getPayrollYear() + "年" + sheet.getPayrollMonth() + "月工资");
        voucher.setReceiptNum(1);
        voucher.setCreateMember(user == null ? null : user.getId());
        voucher.setCreateDate(new Date());
        voucher.setVoucherDate(date);
        voucher.setAccountSetsId(accountSetsId);
        voucher.setCarryForward(false);
        return voucher;
    }

    private VoucherDetails voucherDetail(Subject subject, String summary, Double debit, Double credit, PayrollDetail payrollDetail) {
        VoucherDetails detail = new VoucherDetails();
        detail.setSummary(summary);
        detail.setSubjectId(subject.getId());
        detail.setSubjectCode(subject.getCode());
        detail.setSubjectName(subject.getCode() + "-" + subject.getName());
        detail.setDebitAmount(debit);
        detail.setCreditAmount(credit);
        detail.setAuxiliaryTitle(payrollDetail.getDepartmentName() + "/" + payrollDetail.getEmployeeName());
        return detail;
    }

    private Subject subject(Integer subjectId, String title) {
        if (subjectId == null) throw new ServiceException(title + "未维护！");
        Subject subject = subjectMapper.selectById(subjectId);
        if (subject == null) throw new ServiceException(title + "不存在！");
        return subject;
    }

    private PayrollPeriod period(Integer accountSetsId, Integer year, Integer month) {
        LambdaQueryWrapper<PayrollPeriod> qw = Wrappers.lambdaQuery();
        qw.eq(PayrollPeriod::getAccountSetsId, accountSetsId).eq(PayrollPeriod::getPayrollYear, year).eq(PayrollPeriod::getPayrollMonth, month);
        PayrollPeriod period = payrollPeriodMapper.selectOne(qw);
        if (period != null) return period;
        Calendar c = Calendar.getInstance();
        c.set(Calendar.YEAR, year); c.set(Calendar.MONTH, month - 1);
        period = new PayrollPeriod();
        period.setPayrollYear(year); period.setPayrollMonth(month);
        period.setBeginDate(DateUtil.getMonthBegin(c.getTime()));
        period.setEndDate(DateUtil.getMonthEnd(c.getTime()));
        period.setStatus("OPEN"); period.setAccountSetsId(accountSetsId);
        payrollPeriodMapper.insert(period);
        return period;
    }

    private PayrollSheet sheet(Integer accountSetsId, PayrollPeriod period) {
        LambdaQueryWrapper<PayrollSheet> qw = Wrappers.lambdaQuery();
        qw.eq(PayrollSheet::getAccountSetsId, accountSetsId).eq(PayrollSheet::getPeriodId, period.getId());
        PayrollSheet sheet = payrollSheetMapper.selectOne(qw);
        if (sheet != null) return sheet;
        sheet = new PayrollSheet();
        sheet.setPeriodId(period.getId()); sheet.setPayrollYear(period.getPayrollYear()); sheet.setPayrollMonth(period.getPayrollMonth());
        sheet.setSheetNo(period.getPayrollYear() + String.format("%02d", period.getPayrollMonth()));
        sheet.setSheetName(period.getPayrollYear() + "年" + period.getPayrollMonth() + "月工资表");
        sheet.setStatus("DRAFT"); sheet.setAccountSetsId(accountSetsId);
        payrollSheetMapper.insert(sheet);
        return sheet;
    }

    private PayrollSheet loadSheet(Integer accountSetsId, Integer sheetId) {
        PayrollSheet sheet = payrollSheetMapper.selectById(sheetId);
        if (sheet == null || !accountSetsId.equals(sheet.getAccountSetsId())) throw new ServiceException("工资表不存在！");
        return sheet;
    }

    private List<Employee> employees(Integer accountSetsId) {
        LambdaQueryWrapper<Employee> qw = Wrappers.lambdaQuery();
        qw.eq(Employee::getAccountSetsId, accountSetsId).ne(Employee::getStatus, "LEAVED").orderByAsc(Employee::getEmployeeNo);
        return employeeMapper.selectList(qw);
    }

    private List<PayrollItem> items(Integer accountSetsId) {
        LambdaQueryWrapper<PayrollItem> qw = Wrappers.lambdaQuery();
        qw.eq(PayrollItem::getAccountSetsId, accountSetsId).eq(PayrollItem::getEnabled, true).orderByAsc(PayrollItem::getSortNo).orderByAsc(PayrollItem::getId);
        return payrollItemMapper.selectList(qw);
    }

    private void saveItem(Integer accountSetsId, String code, String name, String type, String formula, Integer sortNo, Boolean system) {
        PayrollItem item = new PayrollItem();
        item.setAccountSetsId(accountSetsId); item.setItemCode(code); item.setItemName(name); item.setItemType(type);
        item.setFormula(formula); item.setSortNo(sortNo); item.setEnabled(true); item.setSystemItem(system);
        payrollItemMapper.insert(item);
    }

    private Map<String, Double> configValues(Integer accountSetsId) {
        Map<String, Double> values = new HashMap<>();
        LambdaQueryWrapper<PayrollConfig> qw = Wrappers.lambdaQuery();
        qw.eq(PayrollConfig::getAccountSetsId, accountSetsId).eq(PayrollConfig::getEnabled, true);
        for (PayrollConfig config : payrollConfigMapper.selectList(qw)) {
            String prefix = config.getConfigType() == null ? config.getConfigName() : config.getConfigType();
            if (prefix == null) continue;
            values.put(prefix + "_PERSONAL_RATE", value(config.getPersonalRate()));
            values.put(prefix + "_COMPANY_RATE", value(config.getCompanyRate()));
            values.put(prefix + "_BASE", value(config.getBaseAmount()));
            values.put(prefix + "_THRESHOLD", value(config.getThresholdAmount()));
        }
        return values;
    }

    private PayrollDetail detail(PayrollSheet sheet, Employee employee, PayrollItem item, double amount) {
        PayrollDetail detail = new PayrollDetail();
        detail.setSheetId(sheet.getId()); detail.setEmployeeId(employee.getId()); detail.setEmployeeNo(employee.getEmployeeNo());
        detail.setEmployeeName(employee.getEmployeeName()); detail.setDepartmentId(employee.getDepartmentId()); detail.setDepartmentName(employee.getDepartmentName());
        detail.setItemId(item.getId()); detail.setItemCode(item.getItemCode()); detail.setItemName(item.getItemName()); detail.setItemType(item.getItemType());
        detail.setAmount(round(amount)); detail.setExpenseSubjectId(item.getExpenseSubjectId() == null ? employee.getExpenseSubjectId() : item.getExpenseSubjectId());
        detail.setAccountSetsId(sheet.getAccountSetsId());
        return detail;
    }

    private double calculateItem(PayrollItem item, Map<String, Double> values) {
        String formula = item.getFormula();
        if (formula == null || formula.trim().isEmpty()) return 0d;
        if (values.containsKey(formula)) return round(values.get(formula));
        Matcher matcher = TOKEN.matcher(formula);
        StringBuffer sb = new StringBuffer();
        while (matcher.find()) {
            String token = matcher.group();
            if (values.containsKey(token)) {
                matcher.appendReplacement(sb, String.valueOf(value(values.get(token))));
            }
        }
        matcher.appendTail(sb);
        try {
            return round(new FormulaParser(sb.toString()).parse());
        } catch (RuntimeException e) {
            throw new ServiceException(item.getItemName() + "公式错误：" + formula);
        }
    }

    private void refreshSheetAmount(PayrollSheet sheet) {
        List<PayrollDetail> details = detailList(sheet.getId(), sheet.getAccountSetsId());
        double gross = details.stream().filter(d -> "EARNING".equals(d.getItemType())).mapToDouble(d -> value(d.getAmount())).sum();
        double deduction = details.stream().filter(d -> "DEDUCTION".equals(d.getItemType())).mapToDouble(d -> value(d.getAmount())).sum();
        double net = details.stream().filter(d -> "NET".equals(d.getItemType())).mapToDouble(d -> value(d.getAmount())).sum();
        sheet.setGrossAmount(round(gross)); sheet.setDeductionAmount(round(deduction)); sheet.setNetAmount(round(net));
        payrollSheetMapper.updateById(sheet);
    }

    private List<Map<String, Object>> payrollMatrix(Integer sheetId, Integer accountSetsId) {
        List<PayrollDetail> details = detailList(sheetId, accountSetsId);
        Map<Integer, Map<String, Object>> rows = new LinkedHashMap<>();
        for (PayrollDetail detail : details) {
            Map<String, Object> row = rows.computeIfAbsent(detail.getEmployeeId(), k -> new LinkedHashMap<>());
            row.put("employeeId", detail.getEmployeeId()); row.put("employeeNo", detail.getEmployeeNo()); row.put("employeeName", detail.getEmployeeName()); row.put("departmentName", detail.getDepartmentName());
            row.put(detail.getItemCode(), detail.getAmount());
        }
        return new ArrayList<>(rows.values());
    }

    private long detailCount(Integer sheetId, Integer accountSetsId) {
        LambdaQueryWrapper<PayrollDetail> qw = Wrappers.lambdaQuery();
        qw.eq(PayrollDetail::getSheetId, sheetId).eq(PayrollDetail::getAccountSetsId, accountSetsId);
        return payrollDetailMapper.selectCount(qw);
    }

    private List<PayrollDetail> detailList(Integer sheetId, Integer accountSetsId) {
        LambdaQueryWrapper<PayrollDetail> qw = Wrappers.lambdaQuery();
        qw.eq(PayrollDetail::getSheetId, sheetId).eq(PayrollDetail::getAccountSetsId, accountSetsId).orderByAsc(PayrollDetail::getEmployeeNo).orderByAsc(PayrollDetail::getId);
        return payrollDetailMapper.selectList(qw);
    }

    private void deleteDetails(Integer sheetId, Integer accountSetsId) {
        LambdaQueryWrapper<PayrollDetail> qw = Wrappers.lambdaQuery();
        qw.eq(PayrollDetail::getSheetId, sheetId).eq(PayrollDetail::getAccountSetsId, accountSetsId);
        payrollDetailMapper.delete(qw);
    }

    private static class FormulaParser {
        private final String expression;
        private int pos = -1;
        private int ch;

        FormulaParser(String expression) {
            this.expression = expression == null ? "0" : expression;
        }

        double parse() {
            nextChar();
            double value = parseExpression();
            if (pos < expression.length()) throw new IllegalArgumentException("Unexpected: " + (char) ch);
            return value;
        }

        private void nextChar() {
            ch = (++pos < expression.length()) ? expression.charAt(pos) : -1;
        }

        private boolean eat(int charToEat) {
            while (ch == ' ') nextChar();
            if (ch == charToEat) {
                nextChar();
                return true;
            }
            return false;
        }

        private double parseExpression() {
            double x = parseTerm();
            for (;;) {
                if (eat('+')) x += parseTerm();
                else if (eat('-')) x -= parseTerm();
                else return x;
            }
        }

        private double parseTerm() {
            double x = parseFactor();
            for (;;) {
                if (eat('*')) x *= parseFactor();
                else if (eat('/')) x /= parseFactor();
                else return x;
            }
        }

        private double parseFactor() {
            if (eat('+')) return parseFactor();
            if (eat('-')) return -parseFactor();
            double x;
            int startPos = this.pos;
            if (eat('(')) {
                x = parseExpression();
                eat(')');
            } else if ((ch >= '0' && ch <= '9') || ch == '.') {
                while ((ch >= '0' && ch <= '9') || ch == '.') nextChar();
                x = Double.parseDouble(expression.substring(startPos, this.pos));
            } else if (ch >= 'a' && ch <= 'z') {
                while (ch >= 'a' && ch <= 'z') nextChar();
                String func = expression.substring(startPos, this.pos);
                eat('(');
                double a = parseExpression();
                eat(',');
                double b = parseExpression();
                eat(')');
                if ("max".equals(func)) x = Math.max(a, b);
                else if ("min".equals(func)) x = Math.min(a, b);
                else throw new IllegalArgumentException("Unknown function: " + func);
            } else {
                throw new IllegalArgumentException("Unexpected: " + (char) ch);
            }
            return x;
        }
    }

    private double value(Double value) { return value == null ? 0d : value; }
    private double round(Double value) { return Math.round(value(value) * 100d) / 100d; }
}
