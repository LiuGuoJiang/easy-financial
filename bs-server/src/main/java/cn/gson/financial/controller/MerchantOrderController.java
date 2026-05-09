package cn.gson.financial.controller;

import cn.gson.financial.base.AdminCrudController;
import cn.gson.financial.kernel.model.entity.MerchantOrder;
import cn.gson.financial.kernel.service.MerchantOrderService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 商业化运营后台 - ORDER 管理。
 */
@RestController
@RequestMapping("/admin/orders")
public class MerchantOrderController extends AdminCrudController<MerchantOrderService, MerchantOrder> {

    @Override
    protected String targetType() {
        return "ORDER";
    }
}
