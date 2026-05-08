package cn.gson.financial.kernel.service.impl;

import cn.gson.financial.kernel.exception.ServiceException;
import cn.gson.financial.kernel.model.entity.*;
import cn.gson.financial.kernel.model.mapper.*;
import cn.gson.financial.kernel.model.vo.UserVo;
import cn.gson.financial.kernel.service.ReceiptBillService;
import cn.gson.financial.kernel.service.VoucherService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;

@Service
@AllArgsConstructor
public class ReceiptBillServiceImpl extends ServiceImpl<ReceiptBillMapper, ReceiptBill> implements ReceiptBillService {
    private FundAccountMapper fundAccountMapper;
    private FundCategoryMapper fundCategoryMapper;
    private FundFlowMapper fundFlowMapper;
    private SubjectMapper subjectMapper;
    private VoucherService voucherService;
    private VoucherMapper voucherMapper;

    @Override
    public boolean save(ReceiptBill entity) {
        if (entity.getStatus() == null) {
            entity.setStatus("DRAFT");
        }
        if (entity.getBillDate() == null) {
            entity.setBillDate(new Date());
        }
        return super.save(entity);
    }

    @Override
    @Transactional
    public void audit(Integer accountSetsId, Integer id, UserVo user, Boolean generateVoucher) {
        ReceiptBill bill = getBill(accountSetsId, id);
        if ("AUDITED".equals(bill.getStatus())) {
            throw new ServiceException("收款单已审核！");
        }
        Integer voucherId = Boolean.TRUE.equals(generateVoucher) ? createVoucher(bill, user) : null;
        FundAccount account = fundAccountMapper.selectById(bill.getAccountId());
        double balance = (account.getCurrentBalance() == null ? 0d : account.getCurrentBalance()) + bill.getAmount();
        account.setCurrentBalance(balance);
        fundAccountMapper.updateById(account);

        FundFlow flow = new FundFlow();
        flow.setFlowNo("SK" + bill.getBillNo());
        flow.setFlowDate(bill.getBillDate());
        flow.setAccountId(bill.getAccountId());
        flow.setCategoryId(bill.getCategoryId());
        flow.setDirection("IN");
        flow.setAmount(bill.getAmount());
        flow.setBalance(balance);
        flow.setSourceType("RECEIPT");
        flow.setSourceId(bill.getId());
        flow.setVoucherId(voucherId);
        flow.setCounterparty(bill.getPayer());
        flow.setSummary(bill.getSummary());
        flow.setReconciled(false);
        flow.setAccountSetsId(accountSetsId);
        fundFlowMapper.insert(flow);

        bill.setStatus("AUDITED");
        bill.setVoucherId(voucherId);
        bill.setAuditMemberId(user.getId());
        bill.setAuditMemberName(user.getRealName() == null ? user.getNickname() : user.getRealName());
        bill.setAuditDate(new Date());
        baseMapper.updateById(bill);
    }

    @Override
    @Transactional
    public void cancelAudit(Integer accountSetsId, Integer id) {
        ReceiptBill bill = getBill(accountSetsId, id);
        if (!"AUDITED".equals(bill.getStatus())) {
            throw new ServiceException("收款单未审核！");
        }
        checkVoucher(bill.getVoucherId());
        LambdaQueryWrapper<FundFlow> flowQw = Wrappers.lambdaQuery();
        flowQw.eq(FundFlow::getAccountSetsId, accountSetsId).eq(FundFlow::getSourceType, "RECEIPT").eq(FundFlow::getSourceId, id);
        FundFlow flow = fundFlowMapper.selectOne(flowQw);
        if (flow != null && Boolean.TRUE.equals(flow.getReconciled())) {
            throw new ServiceException("收款流水已对账，不能反审核！");
        }
        if (flow != null) {
            fundFlowMapper.deleteById(flow.getId());
        }
        FundAccount account = fundAccountMapper.selectById(bill.getAccountId());
        account.setCurrentBalance((account.getCurrentBalance() == null ? 0d : account.getCurrentBalance()) - bill.getAmount());
        fundAccountMapper.updateById(account);
        bill.setStatus("DRAFT");
        bill.setAuditMemberId(null);
        bill.setAuditMemberName(null);
        bill.setAuditDate(null);
        baseMapper.updateById(bill);
    }

    private ReceiptBill getBill(Integer accountSetsId, Integer id) {
        ReceiptBill bill = baseMapper.selectById(id);
        if (bill == null || !accountSetsId.equals(bill.getAccountSetsId())) {
            throw new ServiceException("收款单不存在！");
        }
        return bill;
    }

    private Integer createVoucher(ReceiptBill bill, UserVo user) {
        FundAccount account = fundAccountMapper.selectById(bill.getAccountId());
        FundCategory category = fundCategoryMapper.selectById(bill.getCategoryId());
        if (account == null || account.getSubjectId() == null || category == null || category.getSubjectId() == null) {
            throw new ServiceException("资金账户和资金类别必须维护会计科目！");
        }
        Subject accountSubject = subjectMapper.selectById(account.getSubjectId());
        Subject categorySubject = subjectMapper.selectById(category.getSubjectId());
        Voucher voucher = baseVoucher(bill.getAccountSetsId(), bill.getBillDate(), bill.getSummary(), user);
        voucher.setDetails(Arrays.asList(detail(accountSubject, bill.getSummary(), bill.getAmount(), null), detail(categorySubject, bill.getSummary(), null, bill.getAmount())));
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

    private void checkVoucher(Integer voucherId) {
        if (voucherId == null) {
            return;
        }
        Voucher voucher = voucherMapper.selectById(voucherId);
        if (voucher != null && voucher.getAuditMemberId() != null) {
            throw new ServiceException("关联凭证已审核，不能反审核资金单据！");
        }
    }
}
