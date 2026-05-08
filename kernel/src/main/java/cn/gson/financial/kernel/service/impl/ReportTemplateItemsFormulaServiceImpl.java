package cn.gson.financial.kernel.service.impl;

import cn.gson.financial.kernel.model.entity.ReportTemplateItems;
import cn.gson.financial.kernel.model.entity.ReportTemplateItemsFormula;
import cn.gson.financial.kernel.model.entity.Subject;
import cn.gson.financial.kernel.model.mapper.ReportTemplateItemsFormulaMapper;
import cn.gson.financial.kernel.model.mapper.ReportTemplateItemsMapper;
import cn.gson.financial.kernel.model.mapper.SubjectMapper;
import cn.gson.financial.kernel.service.ReportTemplateItemsFormulaService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
@AllArgsConstructor
public class ReportTemplateItemsFormulaServiceImpl extends ServiceImpl<ReportTemplateItemsFormulaMapper, ReportTemplateItemsFormula> implements ReportTemplateItemsFormulaService {

    private SubjectMapper subjectMapper;

    private ReportTemplateItemsMapper itemsMapper;

    @Override
    public int batchInsert(List<ReportTemplateItemsFormula> list) {
        return baseMapper.batchInsert(list);
    }

    @Override
    public List<String> validateFormulas(ReportTemplateItems item, List<ReportTemplateItemsFormula> formulas, Integer accountSetsId) {
        List<String> errors = new ArrayList<>();
        if (item == null) {
            errors.add("报表项目不存在");
            return errors;
        }
        if (item.getIsClassified() != null && item.getIsClassified()) {
            if (formulas != null && !formulas.isEmpty()) {
                errors.add(item.getTitle() + " 是归类项，不能配置公式");
            }
            return errors;
        }
        if (formulas == null || formulas.isEmpty()) {
            errors.add(item.getTitle() + " 未配置公式");
            return errors;
        }

        Set<String> repeated = new HashSet<>();
        for (ReportTemplateItemsFormula formula : formulas) {
            validateFormula(item, formula, accountSetsId, repeated, errors);
        }
        return errors;
    }

    private void validateFormula(ReportTemplateItems item, ReportTemplateItemsFormula formula, Integer accountSetsId, Set<String> repeated, List<String> errors) {
        if (formula == null) {
            errors.add(item.getTitle() + " 存在空公式");
            return;
        }
        if (formula.getCalculation() == null || !("+".equals(formula.getCalculation().toString()) || "-".equals(formula.getCalculation().toString()))) {
            errors.add(item.getTitle() + " 的运算符只能是 + 或 -");
        }
        if (formula.getFromTag() == null || formula.getFromTag().trim().isEmpty()) {
            errors.add(item.getTitle() + " 存在未选择取数来源的公式");
            return;
        }
        String duplicateKey = formula.getFromTag() + ":" + formula.getCalculation() + ":" + formula.getAccessRules();
        if (!repeated.add(duplicateKey)) {
            errors.add(item.getTitle() + " 存在重复公式：" + formula.getFromTag());
        }

        if (item.getSources() != null && item.getSources() == 0) {
            validateSubjectFormula(item, formula, accountSetsId, errors);
        } else if (item.getSources() != null && item.getSources() == 1) {
            validateInnerFormula(item, formula, errors);
        } else {
            errors.add(item.getTitle() + " 未设置取数来源");
        }
    }

    private void validateSubjectFormula(ReportTemplateItems item, ReportTemplateItemsFormula formula, Integer accountSetsId, List<String> errors) {
        if (formula.getAccessRules() == null || formula.getAccessRules() < 0 || formula.getAccessRules() > 5) {
            errors.add(item.getTitle() + " 的取数规则不正确");
        }
        try {
            Integer subjectId = Integer.parseInt(formula.getFromTag());
            Subject subject = subjectMapper.selectById(subjectId);
            if (subject == null || !accountSetsId.equals(subject.getAccountSetsId())) {
                errors.add(item.getTitle() + " 引用的科目不存在或不属于当前账套：" + formula.getFromTag());
            }
        } catch (NumberFormatException e) {
            errors.add(item.getTitle() + " 的科目标识不正确：" + formula.getFromTag());
        }
    }

    private void validateInnerFormula(ReportTemplateItems item, ReportTemplateItemsFormula formula, List<String> errors) {
        try {
            Integer fromItemId = Integer.parseInt(formula.getFromTag());
            if (fromItemId.equals(item.getId())) {
                errors.add(item.getTitle() + " 的表内公式不能引用自身");
                return;
            }
            LambdaQueryWrapper<ReportTemplateItems> qw = Wrappers.lambdaQuery();
            qw.eq(ReportTemplateItems::getId, fromItemId);
            qw.eq(ReportTemplateItems::getTemplateId, item.getTemplateId());
            ReportTemplateItems fromItem = itemsMapper.selectOne(qw);
            if (fromItem == null || (fromItem.getIsClassified() != null && fromItem.getIsClassified())) {
                errors.add(item.getTitle() + " 引用的报表项目不存在或为归类项：" + formula.getFromTag());
            }
        } catch (NumberFormatException e) {
            errors.add(item.getTitle() + " 的表内项目标识不正确：" + formula.getFromTag());
        }
    }
}
