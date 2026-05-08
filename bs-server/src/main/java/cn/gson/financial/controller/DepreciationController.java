package cn.gson.financial.controller;

import cn.gson.financial.base.BaseCrudController;
import cn.gson.financial.kernel.controller.JsonResult;
import cn.gson.financial.kernel.model.entity.DepreciationVoucherRecord;
import cn.gson.financial.kernel.service.DepreciationService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/fixed-assets/depreciation")
public class DepreciationController extends BaseCrudController<DepreciationService, DepreciationVoucherRecord> {
    @PostMapping("generate")
    public JsonResult generate(@RequestParam Integer year, @RequestParam Integer month, @RequestParam(defaultValue = "true") Boolean generateVoucher) {
        return JsonResult.successful(this.service.generateMonthly(this.accountSetsId, year, month, this.currentUser, generateVoucher));
    }

    @GetMapping("check")
    public JsonResult check(@RequestParam Integer year, @RequestParam Integer month) {
        return JsonResult.successful(this.service.isDepreciationGenerated(this.accountSetsId, year, month));
    }
}
