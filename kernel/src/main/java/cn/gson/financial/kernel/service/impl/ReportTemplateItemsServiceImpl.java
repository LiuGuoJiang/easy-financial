package cn.gson.financial.kernel.service.impl;

import cn.gson.financial.kernel.exception.ServiceException;
import cn.gson.financial.kernel.model.entity.ReportTemplate;
import cn.gson.financial.kernel.model.entity.ReportTemplateItems;
import cn.gson.financial.kernel.model.entity.ReportTemplateItemsFormula;
import cn.gson.financial.kernel.model.mapper.ReportTemplateItemsFormulaMapper;
import cn.gson.financial.kernel.model.mapper.ReportTemplateItemsMapper;
import cn.gson.financial.kernel.model.mapper.ReportTemplateMapper;
import cn.gson.financial.kernel.service.ReportTemplateItemsFormulaService;
import cn.gson.financial.kernel.service.ReportTemplateItemsService;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

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
public class ReportTemplateItemsServiceImpl extends ServiceImpl<ReportTemplateItemsMapper, ReportTemplateItems> implements ReportTemplateItemsService {

    private ReportTemplateItemsFormulaMapper formulaMapper;

    private ReportTemplateMapper templateMapper;

    private ReportTemplateItemsFormulaService formulaService;

    @Override
    public int batchInsert(List<ReportTemplateItems> list) {
        return baseMapper.batchInsert(list);
    }

    @Override
    public List<ReportTemplateItems> list(Wrapper<ReportTemplateItems> queryWrapper) {
        QueryWrapper<ReportTemplateItems> qw = (QueryWrapper) queryWrapper;
        qw.orderByAsc("pos");
        return super.list(qw);
    }

    @Override
    public boolean save(ReportTemplateItems entity) {
        ensureTemplateAccess(entity.getTemplateId(), getTemplateAccountSetsId(entity.getTemplateId()));
        sanitize(entity);
        return super.save(entity);
    }

    @Override
    public boolean updateById(ReportTemplateItems entity) {
        ReportTemplateItems old = baseMapper.selectById(entity.getId());
        if (old == null) {
            throw new ServiceException("报表项目不存在");
        }
        if (entity.getTemplateId() == null) {
            entity.setTemplateId(old.getTemplateId());
        }
        sanitize(entity);
        if (entity.getSources() != null && old.getSources() != null && !old.getSources().equals(entity.getSources())) {
            deleteItemFormula(entity.getId(), null);
        }
        return super.updateById(entity);
    }

    @Override
    public boolean update(ReportTemplateItems entity, Wrapper<ReportTemplateItems> updateWrapper) {
        sanitize(entity);
        ReportTemplateItems templateItems = baseMapper.selectOne(updateWrapper);
        if (templateItems != null && entity.getSources() != null && !templateItems.getSources().equals(entity.getSources())) {
            deleteItemFormula(entity.getId(), null);
        }
        return super.update(entity, updateWrapper);
    }

    @Override
    public ReportTemplateItems ensureItemAccess(Integer templateItemsId, Integer accountSetsId) {
        if (templateItemsId == null) {
            throw new ServiceException("报表项目不存在");
        }
        ReportTemplateItems item = baseMapper.selectById(templateItemsId);
        if (item == null) {
            throw new ServiceException("报表项目不存在");
        }
        ensureTemplateAccess(item.getTemplateId(), accountSetsId);
        return item;
    }

    @Override
    public void ensureTemplateAccess(Integer templateId, Integer accountSetsId) {
        if (templateId == null || accountSetsId == null) {
            throw new ServiceException("报表模板不存在或无权访问");
        }
        LambdaQueryWrapper<ReportTemplate> qw = Wrappers.lambdaQuery();
        qw.eq(ReportTemplate::getId, templateId);
        qw.eq(ReportTemplate::getAccountSetsId, accountSetsId);
        if (templateMapper.selectCount(qw) == 0) {
            throw new ServiceException("报表模板不存在或无权访问");
        }
    }

    @Override
    @Transactional
    public void removeForAccount(Integer templateItemsId, Integer accountSetsId) {
        ReportTemplateItems item = ensureItemAccess(templateItemsId, accountSetsId);
        LambdaQueryWrapper<ReportTemplateItemsFormula> formulaQw = Wrappers.lambdaQuery();
        formulaQw.eq(ReportTemplateItemsFormula::getAccountSetsId, accountSetsId);
        formulaQw.eq(ReportTemplateItemsFormula::getTemplateItemsId, item.getId());
        formulaMapper.delete(formulaQw);
        baseMapper.deleteById(item.getId());
    }

    /**
     * 保存公式信息
     *
     * @param formulas
     * @param accountSetsId
     */
    @Override
    @Transactional
    public List<String> saveFormula(Integer templateItemsId, List<ReportTemplateItemsFormula> formulas, Integer accountSetsId) {
        ReportTemplateItems item = ensureItemAccess(templateItemsId, accountSetsId);
        List<String> errors = formulaService.validateFormulas(item, formulas, accountSetsId);
        if (!errors.isEmpty()) {
            return errors;
        }
        LambdaQueryWrapper<ReportTemplateItemsFormula> qw = Wrappers.lambdaQuery();
        qw.eq(ReportTemplateItemsFormula::getAccountSetsId, accountSetsId);
        qw.eq(ReportTemplateItemsFormula::getTemplateItemsId, templateItemsId);
        formulaMapper.delete(qw);

        if (formulas != null && formulas.size() > 0) {
            formulas.forEach(f -> {
                f.setId(null);
                f.setTemplateId(item.getTemplateId());
                f.setTemplateItemsId(item.getId());
                f.setAccountSetsId(accountSetsId);
                if (item.getSources() != null && item.getSources() == 1) {
                    f.setAccessRules(null);
                }
            });
            formulaMapper.batchInsert(formulas);
        }
        return errors;
    }

    private void sanitize(ReportTemplateItems entity) {
        if (entity.getIsClassified() != null && entity.getIsClassified()) {
            entity.setLineNum(null);
            entity.setSources(null);
            entity.setType(null);
        } else {
            if (entity.getSources() == null) {
                entity.setSources(0);
            }
            if (entity.getLineNum() == null) {
                entity.setLineNum(-1);
            }
        }
        if (entity.getLevel() == null) {
            entity.setLevel(1);
        }
        if (entity.getPos() == null) {
            entity.setPos(1);
        }
        if (entity.getIsBolder() == null) {
            entity.setIsBolder(false);
        }
        if (entity.getIsFolding() == null) {
            entity.setIsFolding(false);
        }
        if (entity.getIsClassified() == null) {
            entity.setIsClassified(false);
        }
    }

    private Integer getTemplateAccountSetsId(Integer templateId) {
        if (templateId == null) {
            return null;
        }
        ReportTemplate template = templateMapper.selectById(templateId);
        return template == null ? null : template.getAccountSetsId();
    }

    private void deleteItemFormula(Integer itemId, Integer accountSetsId) {
        LambdaQueryWrapper<ReportTemplateItemsFormula> qw = Wrappers.lambdaQuery();
        qw.eq(ReportTemplateItemsFormula::getTemplateItemsId, itemId);
        if (accountSetsId != null) {
            qw.eq(ReportTemplateItemsFormula::getAccountSetsId, accountSetsId);
        }
        formulaMapper.delete(qw);
    }
}
