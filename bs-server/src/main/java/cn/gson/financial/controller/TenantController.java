package cn.gson.financial.controller;

import cn.gson.financial.base.AdminCrudController;
import cn.gson.financial.kernel.model.entity.Tenant;
import cn.gson.financial.kernel.service.TenantService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 商业化运营后台 - TENANT 管理。
 */
@RestController
@RequestMapping("/admin/tenants")
public class TenantController extends AdminCrudController<TenantService, Tenant> {

    @Override
    protected String targetType() {
        return "TENANT";
    }
}
