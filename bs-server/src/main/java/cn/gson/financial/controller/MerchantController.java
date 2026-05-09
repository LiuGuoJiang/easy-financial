package cn.gson.financial.controller;

import cn.gson.financial.base.AdminCrudController;
import cn.gson.financial.kernel.model.entity.Merchant;
import cn.gson.financial.kernel.service.MerchantService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 商业化运营后台 - MERCHANT 管理。
 */
@RestController
@RequestMapping("/admin/merchants")
public class MerchantController extends AdminCrudController<MerchantService, Merchant> {

    @Override
    protected String targetType() {
        return "MERCHANT";
    }
}
