package cn.gson.financial.controller;

import cn.gson.financial.base.AdminCrudController;
import cn.gson.financial.kernel.model.entity.AuthorizationQuota;
import cn.gson.financial.kernel.service.AuthorizationQuotaService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 商业化运营后台 - AUTHORIZATION 管理。
 */
@RestController
@RequestMapping("/admin/authorizations")
public class AuthorizationQuotaController extends AdminCrudController<AuthorizationQuotaService, AuthorizationQuota> {

    @Override
    protected String targetType() {
        return "AUTHORIZATION";
    }
}
