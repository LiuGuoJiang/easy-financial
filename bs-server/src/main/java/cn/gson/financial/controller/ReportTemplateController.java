package cn.gson.financial.controller;

import cn.gson.financial.annotation.Permissions;
import cn.gson.financial.base.BaseCrudController;
import cn.gson.financial.kernel.common.Roles;
import cn.gson.financial.kernel.controller.JsonResult;
import cn.gson.financial.kernel.exception.ServiceException;
import cn.gson.financial.kernel.model.entity.ReportTemplate;
import cn.gson.financial.kernel.service.ReportTemplateService;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.Map;

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
@Slf4j
@RestController
@RequestMapping("/report/template")
public class ReportTemplateController extends BaseCrudController<ReportTemplateService, ReportTemplate> {

    @Override
    @GetMapping
    @Permissions({Roles.Manager, Roles.Director, Roles.Making, Roles.Cashier, Roles.View})
    public JsonResult list(@RequestParam Map<String, String> params) {
        if (!canManage()) {
            return enabledList();
        }
        return super.list(params);
    }

    @GetMapping("enabled")
    @Permissions({Roles.Manager, Roles.Director, Roles.Making, Roles.Cashier, Roles.View})
    public JsonResult enabledList() {
        return JsonResult.successful(service.enabledList(this.accountSetsId));
    }

    @Override
    @GetMapping("/{id:\\d+}")
    @Permissions({Roles.Manager, Roles.Director, Roles.Making, Roles.Cashier, Roles.View})
    public JsonResult load(@PathVariable Long id) {
        return JsonResult.successful(service.loadForAccount(this.accountSetsId, id, canManage()));
    }

    @Override
    @PostMapping
    @Permissions({Roles.Manager})
    public JsonResult save(@RequestBody ReportTemplate entity) {
        return super.save(entity);
    }

    @Override
    @PutMapping
    @Permissions({Roles.Manager})
    public JsonResult update(@RequestBody ReportTemplate entity) {
        return super.update(entity);
    }

    @Override
    @DeleteMapping("/{id:\\d+}")
    @Permissions({Roles.Manager})
    public JsonResult delete(@PathVariable Long id) {
        try {
            service.removeForAccount(this.accountSetsId, id);
            return JsonResult.successful();
        } catch (ServiceException se) {
            log.error("删除失败！", se);
            return JsonResult.failure(se.getMessage());
        }
    }

    @PostMapping("copy/{id:\\d+}")
    @Permissions({Roles.Manager})
    public JsonResult copy(@PathVariable Long id) {
        return JsonResult.successful(service.copy(this.accountSetsId, id));
    }

    @PostMapping("publish/{id:\\d+}")
    @Permissions({Roles.Manager})
    public JsonResult publish(@PathVariable Long id, @RequestBody PublishForm publishForm) {
        try {
            service.publish(this.accountSetsId, id, publishForm.getEnabled());
            return JsonResult.successful();
        } catch (ServiceException se) {
            return JsonResult.failure(se.getMessage());
        }
    }

    @GetMapping("validate/{id:\\d+}")
    @Permissions({Roles.Manager})
    public JsonResult validate(@PathVariable Long id) {
        return JsonResult.successful(service.validate(this.accountSetsId, id));
    }

    @GetMapping("preview/{id:\\d+}")
    @Permissions({Roles.Manager})
    public JsonResult preview(@PathVariable Long id, @RequestParam Date accountDate) {
        service.loadForAccount(this.accountSetsId, id, true);
        return JsonResult.successful(service.view(this.accountSetsId, id, accountDate));
    }

    @GetMapping("view/{id:\\d+}")
    @Permissions({Roles.Manager, Roles.Director, Roles.Making, Roles.Cashier, Roles.View})
    public JsonResult view(@PathVariable Long id, @RequestParam Date accountDate) {
        service.loadForAccount(this.accountSetsId, id, false);
        return JsonResult.successful(service.view(this.accountSetsId, id, accountDate));
    }

    private boolean canManage() {
        return this.currentUser != null && Roles.Manager.name().equals(this.currentUser.getRole());
    }

    @Data
    static class PublishForm {
        private Boolean enabled;
    }
}
