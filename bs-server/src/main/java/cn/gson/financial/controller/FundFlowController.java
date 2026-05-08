package cn.gson.financial.controller;

import cn.gson.financial.base.BaseCrudController;
import cn.gson.financial.kernel.controller.JsonResult;
import cn.gson.financial.kernel.model.entity.FundFlow;
import cn.gson.financial.kernel.service.FundFlowService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/fund/flow")
public class FundFlowController extends BaseCrudController<FundFlowService, FundFlow> {
    @GetMapping("report")
    public JsonResult report() {
        return JsonResult.successful(this.service.report(this.accountSetsId));
    }
}
