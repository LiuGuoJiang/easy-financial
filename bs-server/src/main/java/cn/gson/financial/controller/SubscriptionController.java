package cn.gson.financial.controller;

import cn.gson.financial.base.AdminCrudController;
import cn.gson.financial.kernel.model.entity.Subscription;
import cn.gson.financial.kernel.service.SubscriptionService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 商业化运营后台 - SUBSCRIPTION 管理。
 */
@RestController
@RequestMapping("/admin/subscriptions")
public class SubscriptionController extends AdminCrudController<SubscriptionService, Subscription> {

    @Override
    protected String targetType() {
        return "SUBSCRIPTION";
    }
}
