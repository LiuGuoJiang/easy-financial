package cn.gson.financial.kernel.service.impl;

import cn.gson.financial.kernel.exception.ServiceException;
import cn.gson.financial.kernel.model.entity.*;
import cn.gson.financial.kernel.model.mapper.*;
import cn.gson.financial.kernel.model.vo.UserVo;
import cn.gson.financial.kernel.service.FixedAssetService;
import cn.gson.financial.kernel.service.VoucherService;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.Date;

@Service
@AllArgsConstructor
public class FixedAssetServiceImpl extends ServiceImpl<FixedAssetCardMapper, FixedAssetCard> implements FixedAssetService {
    private FixedAssetCategoryMapper categoryMapper;
    private FixedAssetChangeRecordMapper changeRecordMapper;
    private FixedAssetDisposalRecordMapper disposalRecordMapper;
    private SubjectMapper subjectMapper;
    private VoucherService voucherService;

    @Override
    public boolean save(FixedAssetCard entity) {
        fillDefaults(entity);
        return super.save(entity);
    }

    @Override
    public boolean updateById(FixedAssetCard entity) {
        fillCalculatedValues(entity);
        return super.updateById(entity);
    }

    @Override
    public boolean update(FixedAssetCard entity, Wrapper<FixedAssetCard> updateWrapper) {
        fillCalculatedValues(entity);
        return super.update(entity, updateWrapper);
    }

    @Override
    @Transactional
    public Integer acquire(Integer accountSetsId, Integer id, UserVo user, Boolean generateVoucher) {
        FixedAssetCard card = getCard(accountSetsId, id);
        if (!"DRAFT".equals(defaultString(card.getStatus(), "DRAFT"))) {
            throw new ServiceException("只有草稿资产卡片可以执行购入入账！");
        }
        Integer voucherId = Boolean.TRUE.equals(generateVoucher) ? createAcquireVoucher(card, user) : null;
        card.setStatus("IN_USE");
        card.setVoucherId(voucherId);
        baseMapper.updateById(card);
        return voucherId;
    }

    @Override
    @Transactional
    public Integer change(Integer accountSetsId, FixedAssetChangeRecord record, UserVo user, Boolean generateVoucher) {
        FixedAssetCard card = getCard(accountSetsId, record.getAssetId());
        if (!"IN_USE".equals(card.getStatus())) {
            throw new ServiceException("只有使用中的资产可以变动！");
        }
        record.setAccountSetsId(accountSetsId);
        if (record.getChangeDate() == null) {
            record.setChangeDate(new Date());
        }
        record.setBeforeValue(card.getOriginalValue());
        if (record.getAfterValue() != null) {
            card.setOriginalValue(record.getAfterValue());
            fillCalculatedValues(card);
        }
        Integer voucherId = Boolean.TRUE.equals(generateVoucher) ? createChangeVoucher(card, record, user) : null;
        record.setVoucherId(voucherId);
        changeRecordMapper.insert(record);
        baseMapper.updateById(card);
        return voucherId;
    }

    @Override
    @Transactional
    public Integer dispose(Integer accountSetsId, FixedAssetDisposalRecord record, UserVo user, Boolean generateVoucher) {
        FixedAssetCard card = getCard(accountSetsId, record.getAssetId());
        if (!"IN_USE".equals(card.getStatus())) {
            throw new ServiceException("只有使用中的资产可以处置！");
        }
        record.setAccountSetsId(accountSetsId);
        if (record.getDisposalDate() == null) {
            record.setDisposalDate(new Date());
        }
        record.setNetValue(card.getNetValue());
        double income = value(record.getDisposalIncome());
        double expense = value(record.getDisposalExpense());
        record.setGainLoss(income - expense - value(card.getNetValue()));
        Integer voucherId = Boolean.TRUE.equals(generateVoucher) ? createDisposalVoucher(card, record, user) : null;
        record.setVoucherId(voucherId);
        disposalRecordMapper.insert(record);
        card.setStatus("DISPOSED");
        baseMapper.updateById(card);
        return voucherId;
    }

    private void fillDefaults(FixedAssetCard entity) {
        if (entity.getCreateDate() == null) {
            entity.setCreateDate(new Date());
        }
        if (entity.getStatus() == null) {
            entity.setStatus("DRAFT");
        }
        if (entity.getStartUseDate() == null) {
            entity.setStartUseDate(defaultDate(entity.getPurchaseDate()));
        }
        if (entity.getDepreciatedMonths() == null) {
            entity.setDepreciatedMonths(0);
        }
        if (entity.getAccumulatedDepreciation() == null) {
            entity.setAccumulatedDepreciation(0d);
        }
        FixedAssetCategory category = entity.getCategoryId() == null ? null : categoryMapper.selectById(entity.getCategoryId());
        if (category != null) {
            if (entity.getDepreciationMethod() == null) entity.setDepreciationMethod(category.getDepreciationMethod());
            if (entity.getUsefulMonths() == null) entity.setUsefulMonths(category.getUsefulMonths());
            if (entity.getNetSalvageRate() == null) entity.setNetSalvageRate(category.getNetSalvageRate());
            if (entity.getFixedAssetSubjectId() == null) entity.setFixedAssetSubjectId(category.getFixedAssetSubjectId());
            if (entity.getAccumulatedDepreciationSubjectId() == null) entity.setAccumulatedDepreciationSubjectId(category.getAccumulatedDepreciationSubjectId());
            if (entity.getDepreciationExpenseSubjectId() == null) entity.setDepreciationExpenseSubjectId(category.getDepreciationExpenseSubjectId());
        }
        fillCalculatedValues(entity);
    }

    private void fillCalculatedValues(FixedAssetCard entity) {
        double original = value(entity.getOriginalValue());
        double salvageRate = value(entity.getNetSalvageRate());
        if (entity.getExpectedSalvage() == null) {
            entity.setExpectedSalvage(original * salvageRate / 100d);
        }
        entity.setNetValue(original - value(entity.getAccumulatedDepreciation()));
    }

    private FixedAssetCard getCard(Integer accountSetsId, Integer id) {
        FixedAssetCard card = baseMapper.selectById(id);
        if (card == null || !accountSetsId.equals(card.getAccountSetsId())) {
            throw new ServiceException("固定资产卡片不存在！");
        }
        return card;
    }

    private Integer createAcquireVoucher(FixedAssetCard card, UserVo user) {
        Subject fixedAssetSubject = subject(card.getFixedAssetSubjectId(), "固定资产科目");
        Voucher voucher = baseVoucher(card.getAccountSetsId(), defaultDate(card.getPurchaseDate()), "购入固定资产：" + card.getAssetName(), user);
        voucher.setDetails(Arrays.asList(detail(fixedAssetSubject, voucher.getRemark(), value(card.getOriginalValue()), null), detail(fixedAssetSubject, voucher.getRemark(), null, value(card.getOriginalValue()))));
        voucherService.save(voucher);
        return voucher.getId();
    }

    private Integer createChangeVoucher(FixedAssetCard card, FixedAssetChangeRecord record, UserVo user) {
        double diff = value(record.getAfterValue()) - value(record.getBeforeValue());
        if (diff == 0d) return null;
        Subject fixedAssetSubject = subject(card.getFixedAssetSubjectId(), "固定资产科目");
        Voucher voucher = baseVoucher(card.getAccountSetsId(), record.getChangeDate(), defaultString(record.getSummary(), "固定资产原值变动：" + card.getAssetName()), user);
        if (diff > 0) {
            voucher.setDetails(Arrays.asList(detail(fixedAssetSubject, voucher.getRemark(), diff, null), detail(fixedAssetSubject, voucher.getRemark(), null, diff)));
        } else {
            voucher.setDetails(Arrays.asList(detail(fixedAssetSubject, voucher.getRemark(), Math.abs(diff), null), detail(fixedAssetSubject, voucher.getRemark(), null, Math.abs(diff))));
        }
        voucherService.save(voucher);
        return voucher.getId();
    }

    private Integer createDisposalVoucher(FixedAssetCard card, FixedAssetDisposalRecord record, UserVo user) {
        Subject fixedAssetSubject = subject(card.getFixedAssetSubjectId(), "固定资产科目");
        Subject accumulatedSubject = subject(card.getAccumulatedDepreciationSubjectId(), "累计折旧科目");
        Voucher voucher = baseVoucher(card.getAccountSetsId(), record.getDisposalDate(), defaultString(record.getSummary(), "处置固定资产：" + card.getAssetName()), user);
        voucher.setDetails(Arrays.asList(
                detail(accumulatedSubject, voucher.getRemark(), value(card.getAccumulatedDepreciation()), null),
                detail(fixedAssetSubject, voucher.getRemark(), value(card.getNetValue()), null),
                detail(fixedAssetSubject, voucher.getRemark(), null, value(card.getOriginalValue()))
        ));
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

    private Date defaultDate(Date date) {
        return date == null ? new Date() : date;
    }

    private String defaultString(String value, String defaultValue) {
        return value == null || value.trim().length() == 0 ? defaultValue : value;
    }

    private double value(Double value) {
        return value == null ? 0d : value;
    }
}
