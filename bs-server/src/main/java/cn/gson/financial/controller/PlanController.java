package cn.gson.financial.controller;

import cn.gson.financial.base.AdminCrudController;
import cn.gson.financial.kernel.model.entity.Plan;
import cn.gson.financial.kernel.service.PlanService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 商业化运营后台 - PLAN 管理。
 */
@RestController
@RequestMapping("/admin/plans")
public class PlanController extends AdminCrudController<PlanService, Plan> {

    @Override
    protected String targetType() {
        return "PLAN";
    }
}
