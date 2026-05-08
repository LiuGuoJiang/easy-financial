package cn.gson.financial.kernel.service.impl;

import cn.gson.financial.kernel.common.DateUtil;
import cn.gson.financial.kernel.common.DoubleValueUtil;
import cn.gson.financial.kernel.exception.ServiceException;
import cn.gson.financial.kernel.model.entity.*;
import cn.gson.financial.kernel.model.mapper.*;
import cn.gson.financial.kernel.model.vo.ReportDataVo;
import cn.gson.financial.kernel.model.vo.VoucherDetailVo;
import cn.gson.financial.kernel.service.ReportTemplateService;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * <p>****************************************************************************</p>
 * <p><b>Copyright © 2010-2019 soho team All Rights Reserved<b></p>
 * <ul style="margin:15px;">
 * <li>Description : cn.gson.financial.kernel.service.impl</li>
 * <li>Version     : 1.0</li>
 * <li>Creation    : 2019年09月05日</li>
 * <li>@author     : ____′↘夏悸</li>
 * </ul>
 * <p>****************************************************************************</p>
 */
@Service
@Slf4j
public class ReportTemplateServiceImpl extends ServiceImpl<ReportTemplateMapper, ReportTemplate> implements ReportTemplateService {

    @Autowired
    private ReportTemplateItemsMapper itemsMapper;

    @Autowired
    private ReportTemplateItemsFormulaMapper formulaMapper;

    @Autowired
    private VoucherMapper voucherMapper;

    @Autowired
    private SubjectMapper subjectMapper;

    @Autowired
    private AccountSetsMapper accountSetsMapper;

    @Autowired
    private cn.gson.financial.kernel.service.ReportTemplateItemsFormulaService formulaService;

    private DateFormat sdf = new SimpleDateFormat("yyyy");

    @Override
    public int batchInsert(List<ReportTemplate> list) {
        return baseMapper.batchInsert(list);
    }



    @Override
    public List<ReportTemplate> enabledList(Integer accountSetsId) {
        LambdaQueryWrapper<ReportTemplate> qw = Wrappers.lambdaQuery();
        qw.eq(ReportTemplate::getAccountSetsId, accountSetsId);
        qw.eq(ReportTemplate::getIsDefault, true);
        qw.orderByAsc(ReportTemplate::getPos, ReportTemplate::getId);
        return baseMapper.selectList(qw);
    }

    @Override
    public ReportTemplate loadForAccount(Integer accountSetsId, Long id, boolean allowDisabled) {
        LambdaQueryWrapper<ReportTemplate> qw = Wrappers.lambdaQuery();
        qw.eq(ReportTemplate::getAccountSetsId, accountSetsId);
        qw.eq(ReportTemplate::getId, id);
        if (!allowDisabled) {
            qw.eq(ReportTemplate::getIsDefault, true);
        }
        ReportTemplate template = this.getOne(qw);
        if (template == null) {
            throw new ServiceException("报表模板不存在或无权访问");
        }
        return template;
    }

    @Override
    @Transactional
    public ReportTemplate copy(Integer accountSetsId, Long id) {
        ReportTemplate source = loadForAccount(accountSetsId, id, true);
        ReportTemplate target = new ReportTemplate();
        target.setName(source.getName() + "-副本");
        target.setTemplateKey(createCopyKey(accountSetsId, source.getTemplateKey()));
        target.setType(source.getType());
        target.setAccountSetsId(accountSetsId);
        target.setIsDefault(false);
        target.setPos(source.getPos());
        baseMapper.insert(target);

        Map<Integer, Integer> itemIdMap = new LinkedHashMap<>();
        for (ReportTemplateItems sourceItem : source.getItems()) {
            ReportTemplateItems targetItem = copyItem(sourceItem, target.getId());
            targetItem.setParentId(null);
            itemsMapper.insert(targetItem);
            itemIdMap.put(sourceItem.getId(), targetItem.getId());
        }
        for (ReportTemplateItems sourceItem : source.getItems()) {
            Integer newId = itemIdMap.get(sourceItem.getId());
            Integer newParentId = itemIdMap.get(sourceItem.getParentId());
            if (newParentId != null) {
                ReportTemplateItems update = new ReportTemplateItems();
                update.setId(newId);
                update.setParentId(newParentId);
                itemsMapper.updateById(update);
            }
            if (sourceItem.getFormulas() != null && !sourceItem.getFormulas().isEmpty()) {
                List<ReportTemplateItemsFormula> copiedFormulas = new ArrayList<>();
                for (ReportTemplateItemsFormula formula : sourceItem.getFormulas()) {
                    ReportTemplateItemsFormula copied = copyFormula(sourceItem, formula, target.getId(), newId, accountSetsId, itemIdMap);
                    copiedFormulas.add(copied);
                }
                formulaMapper.batchInsert(copiedFormulas);
            }
        }
        return loadForAccount(accountSetsId, Long.valueOf(target.getId()), true);
    }

    @Override
    @Transactional
    public void removeForAccount(Integer accountSetsId, Long id) {
        ReportTemplate template = loadForAccount(accountSetsId, id, true);
        List<Integer> itemIds = template.getItems().stream().map(ReportTemplateItems::getId).collect(Collectors.toList());
        if (!itemIds.isEmpty()) {
            LambdaQueryWrapper<ReportTemplateItemsFormula> formulaQw = Wrappers.lambdaQuery();
            formulaQw.eq(ReportTemplateItemsFormula::getAccountSetsId, accountSetsId);
            formulaQw.eq(ReportTemplateItemsFormula::getTemplateId, template.getId());
            formulaMapper.delete(formulaQw);

            LambdaQueryWrapper<ReportTemplateItems> itemsQw = Wrappers.lambdaQuery();
            itemsQw.eq(ReportTemplateItems::getTemplateId, template.getId());
            itemsMapper.delete(itemsQw);
        }
        baseMapper.deleteById(template.getId());
    }

    @Override
    public void publish(Integer accountSetsId, Long id, Boolean enabled) {
        loadForAccount(accountSetsId, id, true);
        if (enabled != null && enabled) {
            List<String> errors = validate(accountSetsId, id);
            if (!errors.isEmpty()) {
                throw new ServiceException("公式校验未通过：" + String.join("；", errors));
            }
        }
        ReportTemplate update = new ReportTemplate();
        update.setId(id.intValue());
        update.setIsDefault(enabled != null && enabled);
        baseMapper.updateById(update);
    }

    @Override
    public List<String> validate(Integer accountSetsId, Long id) {
        ReportTemplate template = loadForAccount(accountSetsId, id, true);
        List<String> errors = new ArrayList<>();
        if (template.getItems() == null || template.getItems().isEmpty()) {
            errors.add("报表模板未配置项目");
            return errors;
        }
        for (ReportTemplateItems item : template.getItems()) {
            if (item.getIsClassified() == null || !item.getIsClassified()) {
                errors.addAll(formulaService.validateFormulas(item, item.getFormulas(), accountSetsId));
            }
        }
        return errors;
    }

    @Override
    public boolean save(ReportTemplate entity) {
        sanitizeTemplate(entity);
        return super.save(entity);
    }

    @Override
    public boolean update(ReportTemplate entity, Wrapper<ReportTemplate> updateWrapper) {
        sanitizeTemplate(entity);
        return super.update(entity, updateWrapper);
    }

    private void sanitizeTemplate(ReportTemplate entity) {
        if (entity.getType() == null) {
            entity.setType(0);
        }
        if (entity.getIsDefault() == null) {
            entity.setIsDefault(false);
        }
        if (entity.getPos() == null) {
            entity.setPos(0);
        }
    }

    private String createCopyKey(Integer accountSetsId, String templateKey) {
        String baseKey = templateKey + "_copy";
        String key = baseKey;
        int index = 1;
        while (existsTemplateKey(accountSetsId, key)) {
            key = baseKey + index++;
        }
        return key;
    }

    private boolean existsTemplateKey(Integer accountSetsId, String key) {
        LambdaQueryWrapper<ReportTemplate> qw = Wrappers.lambdaQuery();
        qw.eq(ReportTemplate::getAccountSetsId, accountSetsId);
        qw.eq(ReportTemplate::getTemplateKey, key);
        return baseMapper.selectCount(qw) > 0;
    }

    private ReportTemplateItems copyItem(ReportTemplateItems sourceItem, Integer templateId) {
        ReportTemplateItems targetItem = new ReportTemplateItems();
        targetItem.setTemplateId(templateId);
        targetItem.setTitle(sourceItem.getTitle());
        targetItem.setLineNum(sourceItem.getLineNum());
        targetItem.setType(sourceItem.getType());
        targetItem.setSources(sourceItem.getSources());
        targetItem.setLevel(sourceItem.getLevel());
        targetItem.setIsBolder(sourceItem.getIsBolder());
        targetItem.setIsFolding(sourceItem.getIsFolding());
        targetItem.setIsClassified(sourceItem.getIsClassified());
        targetItem.setPos(sourceItem.getPos());
        return targetItem;
    }

    private ReportTemplateItemsFormula copyFormula(ReportTemplateItems sourceItem, ReportTemplateItemsFormula formula, Integer templateId, Integer itemId, Integer accountSetsId, Map<Integer, Integer> itemIdMap) {
        ReportTemplateItemsFormula copied = new ReportTemplateItemsFormula();
        copied.setTemplateId(templateId);
        copied.setTemplateItemsId(itemId);
        copied.setAccountSetsId(accountSetsId);
        copied.setCalculation(formula.getCalculation());
        copied.setAccessRules(formula.getAccessRules());
        copied.setFromTag(formula.getFromTag());
        if (sourceItem.getSources() != null && sourceItem.getSources() == 1) {
            Integer remappedItemId = itemIdMap.get(parseInteger(formula.getFromTag()));
            if (remappedItemId != null) {
                copied.setFromTag(String.valueOf(remappedItemId));
            }
        }
        return copied;
    }

    private Integer parseInteger(String val) {
        try {
            return val == null ? null : Integer.parseInt(val);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    /**
     * 计算报表
     *
     * @param accountSetsId
     * @param id
     * @param accountDate
     * @return
     */
    @Override
    public Map<Integer, ReportDataVo> view(Integer accountSetsId, Long id, Date accountDate) {
        AccountSets accountSets = accountSetsMapper.selectById(accountSetsId);

        ReportTemplate template = loadForAccount(accountSetsId, id, true);
        log.info("{}", template);
        Map<Integer, ReportDataVo> dataVoMap = new HashMap<>();
        if (template.getItems() != null) {
            //先计算表外公式
            template.getItems().stream().filter(rti -> rti.getSources() == 0 && !rti.getIsClassified()).forEach(item -> {
                ReportDataVo dataVo = new ReportDataVo();
                dataVo.setItemId(item.getId());

                //获取计算项
                if (item.getFormulas() != null && !item.getFormulas().isEmpty()) {
                    //所有计算项目的科目 id
                    List<String> fromTags = item.getFormulas().stream().map(ReportTemplateItemsFormula::getFromTag).collect(Collectors.toList());
                    List<Subject> subjectList = subjectMapper.selectBatchIds(fromTags);
                    Map<Integer, Subject> subjectMap = subjectList.stream().collect(Collectors.toMap(Subject::getId, subject -> subject));
                    List<String> codes = subjectList.stream().map(Subject::getCode).collect(Collectors.toList());

                    List<VoucherDetailVo> detailVos;

                    if (template.getType() == 0) {
                        //个科目的期年统计数据
                        detailVos = voucherMapper.selectReportStatistical(accountSetsId, codes, DateUtil.getMonthEnd(accountDate));
                    } else {
                        //资产负债查余额
                        detailVos = voucherMapper.selectReportBalanceStatistical(accountSetsId, codes, DateUtil.getMonthEnd(accountDate));
                    }


                    //先根据期年进行分组，再根据科目 id 进行数据转换
                    Map<String, Map<String, VoucherDetailVo>> collect = detailVos.stream().collect(Collectors.groupingBy(VoucherDetailVo::getSummary, Collectors.toMap(VoucherDetailVo::getSubjectCode, vo -> vo)));

                    Map<String, VoucherDetailVo> periodData = collect.getOrDefault("本期", Collections.emptyMap());
                    Map<String, VoucherDetailVo> yearData = collect.getOrDefault("本年", Collections.emptyMap());

                    //如果本年余额为空
                    if ((yearData.isEmpty() || yearData.values().stream().allMatch(vo -> vo.isNull())) && template.getType() == 1 && sdf.format(accountSets.getEnableDate()).equals(sdf.format(accountDate))) {
                        List<VoucherDetailVo> detailInitVos = voucherMapper.selectReportInitBalance(accountSetsId, codes);
                        yearData = detailInitVos.stream().collect(Collectors.groupingBy(VoucherDetailVo::getSummary, Collectors.toMap(VoucherDetailVo::getSubjectCode, vo -> vo))).getOrDefault("本年", Collections.emptyMap());
                    }

                    //计算公式合计
                    dataVo.setCurrentPeriodAmount(getaCombined(subjectMap, item, periodData));
                    dataVo.setCurrentYearAmount(getaCombined(subjectMap, item, yearData));
                }

                dataVoMap.put(item.getId(), dataVo);
            });

            //表内
            template.getItems().stream().filter(rti -> rti.getSources() == 1 && !rti.getIsClassified()).forEach(item -> {
                ReportDataVo dataVo = new ReportDataVo();
                dataVo.setItemId(item.getId());
                Double periodNum = null;
                Double yearNum = null;
                if (item.getFormulas() != null && !item.getFormulas().isEmpty()) {
                    for (ReportTemplateItemsFormula formula : item.getFormulas()) {
                        //获取表内已计算的
                        ReportDataVo dataVo1 = dataVoMap.get(Integer.parseInt(formula.getFromTag()));
                        if (dataVo1 != null) {
                            if (periodNum == null) {
                                periodNum = dataVo1.getCurrentPeriodAmount();
                            } else {
                                switch (formula.getCalculation().toString()) {
                                    case "+":
                                        periodNum += DoubleValueUtil.getNotNullVal(dataVo1.getCurrentPeriodAmount());
                                        break;
                                    case "-":
                                        periodNum -= DoubleValueUtil.getNotNullVal(dataVo1.getCurrentPeriodAmount());
                                        break;
                                }
                            }

                            if (yearNum == null) {
                                yearNum = dataVo1.getCurrentYearAmount();
                            } else {
                                switch (formula.getCalculation().toString()) {
                                    case "+":
                                        yearNum += DoubleValueUtil.getNotNullVal(dataVo1.getCurrentYearAmount());
                                        break;
                                    case "-":
                                        yearNum -= DoubleValueUtil.getNotNullVal(dataVo1.getCurrentYearAmount());
                                        break;
                                }
                            }
                        }
                    }
                }
                dataVo.setCurrentPeriodAmount(periodNum);
                dataVo.setCurrentYearAmount(yearNum);
                dataVoMap.put(item.getId(), dataVo);
            });
        }
        return dataVoMap;
    }

    /**
     * 根据工具合计
     *
     * @param item
     * @param periodData
     * @return
     */
    private Double getaCombined(Map<Integer, Subject> codes, ReportTemplateItems item, Map<String, VoucherDetailVo> periodData) {
        double periodNum = 0d;
        if (periodData == null) {
            return periodNum;
        }
        //计算本期合计
        for (ReportTemplateItemsFormula formula : item.getFormulas()) {
            Subject subject = codes.get(Integer.parseInt(formula.getFromTag()));
            VoucherDetailVo vo = periodData.get(subject.getCode());
            if (vo != null) {
                //0: " 净发生额", 1: "借方发生额", 2: "贷方发生额", 3: "余额", 4: "期末余额", 5: "期初余额"
                double money = 0;
                switch (formula.getAccessRules()) {
                    case 0://净发生额
                    case 3://余额
                        switch (subject.getBalanceDirection() + "") {
                            case "借":
                                money = DoubleValueUtil.getNotNullVal(vo.getDebitAmount()) - DoubleValueUtil.getNotNullVal(vo.getCreditAmount());
                                break;
                            case "贷":
                                money = DoubleValueUtil.getNotNullVal(vo.getCreditAmount()) - DoubleValueUtil.getNotNullVal(vo.getDebitAmount());
                                break;
                        }
                        break;
                    case 1://借方发生额
                        money = DoubleValueUtil.getNotNullVal(vo.getDebitAmount());
                        break;
                    case 2://贷方发生额
                        money = DoubleValueUtil.getNotNullVal(vo.getCreditAmount());
                        break;
                    case 4://借方余额
                        money = DoubleValueUtil.getNotNullVal(vo.getDebitAmount()) - DoubleValueUtil.getNotNullVal(vo.getCreditAmount());
                        break;
                    case 5://贷方余额
                        money = DoubleValueUtil.getNotNullVal(vo.getCreditAmount()) - DoubleValueUtil.getNotNullVal(vo.getDebitAmount());
                        break;
                }

                //计算公式
                switch (formula.getCalculation().toString()) {
                    case "+":
                        periodNum += money;
                        break;
                    case "-":
                        periodNum -= money;
                        break;
                }
            }
        }
        return periodNum;
    }

    @Override
    public ReportTemplate getOne(Wrapper<ReportTemplate> queryWrapper) {
        ReportTemplate template = baseMapper.selectOne(queryWrapper);
        if (template == null) {
            return null;
        }
        LambdaQueryWrapper<ReportTemplateItems> rtiQw = Wrappers.lambdaQuery();
        rtiQw.orderByAsc(ReportTemplateItems::getPos);
        rtiQw.eq(ReportTemplateItems::getTemplateId, template.getId());
        template.setItems(itemsMapper.selectList(rtiQw));

        template.getItems().forEach(it -> {
            LambdaQueryWrapper<ReportTemplateItemsFormula> rtifQw = Wrappers.lambdaQuery();
            rtifQw.eq(ReportTemplateItemsFormula::getAccountSetsId, template.getAccountSetsId());
            rtifQw.eq(ReportTemplateItemsFormula::getTemplateId, template.getId());
            rtifQw.eq(ReportTemplateItemsFormula::getTemplateItemsId, it.getId());
            it.setFormulas(formulaMapper.selectList(rtifQw));
        });

        return template;
    }
}
