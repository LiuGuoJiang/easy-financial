package cn.gson.financial.controller;

import cn.gson.financial.annotation.Permissions;
import cn.gson.financial.base.BaseCrudController;
import cn.gson.financial.kernel.common.Roles;
import cn.gson.financial.kernel.controller.JsonResult;
import cn.gson.financial.kernel.exception.ServiceException;
import cn.gson.financial.kernel.model.entity.ReportTemplateItems;
import cn.gson.financial.kernel.model.entity.ReportTemplateItemsFormula;
import cn.gson.financial.kernel.service.ReportTemplateItemsFormulaService;
import cn.gson.financial.kernel.service.ReportTemplateItemsService;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>****************************************************************************</p>
 * <p><b>Copyright © 2010-2019 soho team All Rights Reserved<b></p>
 * <ul style="margin:15px;">
 * <li>Description : cn.gson.financial.controller</li>
 * <li>Version     : 1.0</li>
 * <li>Creation    : 2019年09月05日</li>
 * <li>@author     : ____′↘夏悸</li>
 * </ul>
 * <p>****************************************************************************</p>
 */
@RestController
@RequestMapping("/report/template/items")
public class ReportTemplateItemsController extends BaseCrudController<ReportTemplateItemsService, ReportTemplateItems> {

    @Autowired
    private ReportTemplateItemsFormulaService formulaService;

    @Override
    @GetMapping("/{id:\\d+}")
    @Permissions({Roles.Manager, Roles.Director, Roles.Making, Roles.Cashier, Roles.View})
    public JsonResult load(@PathVariable Long id) {
        return JsonResult.successful(service.ensureItemAccess(id.intValue(), this.accountSetsId));
    }

    @Override
    @PostMapping
    @Permissions({Roles.Manager})
    public JsonResult save(@RequestBody ReportTemplateItems entity) {
        try {
            service.ensureTemplateAccess(entity.getTemplateId(), this.accountSetsId);
            service.save(entity);
            return JsonResult.successful();
        } catch (ServiceException se) {
            return JsonResult.failure(se.getMessage());
        }
    }

    @Override
    @PutMapping
    @Permissions({Roles.Manager})
    public JsonResult update(@RequestBody ReportTemplateItems entity) {
        try {
            service.ensureItemAccess(entity.getId(), this.accountSetsId);
            service.updateById(entity);
            return JsonResult.successful();
        } catch (ServiceException se) {
            return JsonResult.failure(se.getMessage());
        }
    }

    @Override
    @DeleteMapping("/{id:\\d+}")
    @Permissions({Roles.Manager})
    public JsonResult delete(@PathVariable Long id) {
        try {
            service.removeForAccount(id.intValue(), this.accountSetsId);
            return JsonResult.successful();
        } catch (ServiceException se) {
            return JsonResult.failure(se.getMessage());
        }
    }

    @PostMapping("formula/validate")
    @Permissions({Roles.Manager})
    public JsonResult validateFormula(@RequestBody FormulaForm formulaForm) {
        ReportTemplateItems item = service.ensureItemAccess(formulaForm.getTemplateItemsId(), this.accountSetsId);
        return JsonResult.successful(formulaService.validateFormulas(item, formulaForm.getFormulas(), this.accountSetsId));
    }

    @PostMapping("formula")
    @Permissions({Roles.Manager})
    public JsonResult saveFormula(@RequestBody FormulaForm formulaForm) {
        try {
            List<String> errors = service.saveFormula(formulaForm.getTemplateItemsId(), formulaForm.getFormulas(), this.accountSetsId);
            return JsonResult.successful(errors);
        } catch (ServiceException se) {
            return JsonResult.failure(se.getMessage());
        }
    }

    @Data
    static class FormulaForm {
        List<ReportTemplateItemsFormula> formulas;
        Integer templateItemsId;
    }
}
