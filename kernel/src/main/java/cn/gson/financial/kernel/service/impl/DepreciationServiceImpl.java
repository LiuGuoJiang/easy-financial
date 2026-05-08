package cn.gson.financial.kernel.service.impl;

import cn.gson.financial.kernel.common.DateUtil;
import cn.gson.financial.kernel.exception.ServiceException;
import cn.gson.financial.kernel.model.entity.*;
import cn.gson.financial.kernel.model.mapper.DepreciationVoucherRecordMapper;
import cn.gson.financial.kernel.model.mapper.FixedAssetCardMapper;
import cn.gson.financial.kernel.model.mapper.SubjectMapper;
import cn.gson.financial.kernel.model.vo.UserVo;
import cn.gson.financial.kernel.service.DepreciationService;
import cn.gson.financial.kernel.service.VoucherService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class DepreciationServiceImpl extends ServiceImpl<DepreciationVoucherRecordMapper, DepreciationVoucherRecord> implements DepreciationService {
    private FixedAssetCardMapper fixedAssetCardMapper;
    private SubjectMapper subjectMapper;
    private VoucherService voucherService;

    @Override
    @Transactional
    public List<DepreciationVoucherRecord> generateMonthly(Integer accountSetsId, Integer year, Integer month, UserVo user, Boolean generateVoucher) {
        if (isDepreciationGenerated(accountSetsId, year, month)) {
            throw new ServiceException("当期固定资产折旧已生成！");
        }
        Date depreciationDate = periodEnd(year, month);
        List<FixedAssetCard> cards = depreciationCards(accountSetsId, depreciationDate);
        List<DepreciationVoucherRecord> records = new ArrayList<>();
        for (FixedAssetCard card : cards) {
            double amount = calculateMonthlyDepreciation(card);
            if (amount <= 0d) {
                continue;
            }
            double before = value(card.getAccumulatedDepreciation());
            DepreciationVoucherRecord record = new DepreciationVoucherRecord();
            record.setAssetId(card.getId());
            record.setDepreciationYear(year);
            record.setDepreciationMonth(month);
            record.setDepreciationDate(depreciationDate);
            record.setAmount(amount);
            record.setBeforeAccumulatedDepreciation(before);
            record.setAfterAccumulatedDepreciation(before + amount);
            record.setAccountSetsId(accountSetsId);
            baseMapper.insert(record);

            card.setAccumulatedDepreciation(before + amount);
            card.setDepreciatedMonths((card.getDepreciatedMonths() == null ? 0 : card.getDepreciatedMonths()) + 1);
            card.setNetValue(value(card.getOriginalValue()) - card.getAccumulatedDepreciation());
            fixedAssetCardMapper.updateById(card);
            records.add(record);
        }
        if (Boolean.TRUE.equals(generateVoucher) && !records.isEmpty()) {
            Integer voucherId = createVoucher(accountSetsId, depreciationDate, records, user);
            records.forEach(record -> {
                record.setVoucherId(voucherId);
                baseMapper.updateById(record);
            });
        }
        return records;
    }

    @Override
    public boolean hasDepreciableAssets(Integer accountSetsId, Integer year, Integer month) {
        return !depreciationCards(accountSetsId, periodEnd(year, month)).isEmpty();
    }

    @Override
    public boolean isDepreciationGenerated(Integer accountSetsId, Integer year, Integer month) {
        LambdaQueryWrapper<DepreciationVoucherRecord> qw = Wrappers.lambdaQuery();
        qw.eq(DepreciationVoucherRecord::getAccountSetsId, accountSetsId)
                .eq(DepreciationVoucherRecord::getDepreciationYear, year)
                .eq(DepreciationVoucherRecord::getDepreciationMonth, month);
        return baseMapper.selectCount(qw) > 0;
    }

    private List<FixedAssetCard> depreciationCards(Integer accountSetsId, Date depreciationDate) {
        LambdaQueryWrapper<FixedAssetCard> qw = Wrappers.lambdaQuery();
        qw.eq(FixedAssetCard::getAccountSetsId, accountSetsId)
                .eq(FixedAssetCard::getStatus, "IN_USE")
                .le(FixedAssetCard::getStartUseDate, depreciationDate);
        List<FixedAssetCard> cards = fixedAssetCardMapper.selectList(qw);
        return cards.stream().filter(card -> value(card.getNetValue()) > value(card.getExpectedSalvage()))
                .filter(card -> card.getUsefulMonths() == null || card.getDepreciatedMonths() == null || card.getDepreciatedMonths() < card.getUsefulMonths())
                .collect(Collectors.toList());
    }

    private double calculateMonthlyDepreciation(FixedAssetCard card) {
        double original = value(card.getOriginalValue());
        double accumulated = value(card.getAccumulatedDepreciation());
        double salvage = card.getExpectedSalvage() == null ? original * value(card.getNetSalvageRate()) / 100d : value(card.getExpectedSalvage());
        int usefulMonths = card.getUsefulMonths() == null || card.getUsefulMonths() <= 0 ? 1 : card.getUsefulMonths();
        int depreciatedMonths = card.getDepreciatedMonths() == null ? 0 : card.getDepreciatedMonths();
        double amount;
        String method = card.getDepreciationMethod() == null ? "STRAIGHT_LINE" : card.getDepreciationMethod();
        if ("DOUBLE_DECLINING".equals(method)) {
            amount = (original - accumulated) * 2d / usefulMonths;
        } else if ("SUM_OF_YEARS_DIGITS".equals(method)) {
            int remainingMonths = usefulMonths - depreciatedMonths;
            double denominator = usefulMonths * (usefulMonths + 1d) / 2d;
            amount = (original - salvage) * remainingMonths / denominator;
        } else {
            amount = (original - salvage) / usefulMonths;
        }
        double max = original - salvage - accumulated;
        return Math.round(Math.min(amount, max) * 100d) / 100d;
    }

    private Integer createVoucher(Integer accountSetsId, Date date, List<DepreciationVoucherRecord> records, UserVo user) {
        Map<Integer, Double> amountByAsset = records.stream().collect(Collectors.toMap(DepreciationVoucherRecord::getAssetId, DepreciationVoucherRecord::getAmount));
        List<FixedAssetCard> cards = fixedAssetCardMapper.selectBatchIds(amountByAsset.keySet());
        Voucher voucher = baseVoucher(accountSetsId, date, "计提固定资产折旧", user);
        List<VoucherDetails> details = new ArrayList<>();
        for (FixedAssetCard card : cards) {
            double amount = amountByAsset.get(card.getId());
            Subject expenseSubject = subject(card.getDepreciationExpenseSubjectId(), "折旧费用科目");
            Subject accumulatedSubject = subject(card.getAccumulatedDepreciationSubjectId(), "累计折旧科目");
            String summary = "计提折旧：" + card.getAssetName();
            details.add(detail(expenseSubject, summary, amount, null));
            details.add(detail(accumulatedSubject, summary, null, amount));
        }
        voucher.setDetails(details);
        voucherService.save(voucher);
        return voucher.getId();
    }

    private Voucher baseVoucher(Integer accountSetsId, Date date, String summary, UserVo user) {
        Voucher voucher = new Voucher();
        voucher.setWord("记");
        voucher.setCode(voucherService.loadCode(accountSetsId, "记", date));
        voucher.setRemark(summary);
        voucher.setReceiptNum(1);
        voucher.setCreateMember(user.getId());
        voucher.setCreateDate(new Date());
        voucher.setAccountSetsId(accountSetsId);
        voucher.setVoucherDate(date);
        voucher.setCarryForward(false);
        return voucher;
    }

    private VoucherDetails detail(Subject subject, String summary, Double debit, Double credit) {
        VoucherDetails detail = new VoucherDetails();
        detail.setSummary(summary);
        detail.setSubjectId(subject.getId());
        detail.setSubjectCode(subject.getCode());
        detail.setSubjectName(subject.getCode() + "-" + subject.getName());
        detail.setDebitAmount(debit);
        detail.setCreditAmount(credit);
        return detail;
    }

    private Subject subject(Integer subjectId, String name) {
        if (subjectId == null) throw new ServiceException(name + "未维护！");
        Subject subject = subjectMapper.selectById(subjectId);
        if (subject == null) throw new ServiceException(name + "不存在！");
        return subject;
    }

    private Date periodEnd(Integer year, Integer month) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month - 1);
        return DateUtil.getMonthEnd(calendar.getTime());
    }

    private double value(Double value) {
        return value == null ? 0d : value;
    }
}
