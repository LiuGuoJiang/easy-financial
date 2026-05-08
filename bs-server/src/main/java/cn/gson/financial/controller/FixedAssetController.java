package cn.gson.financial.controller;

import cn.gson.financial.base.BaseCrudController;
import cn.gson.financial.kernel.controller.JsonResult;
import cn.gson.financial.kernel.model.entity.FixedAssetCard;
import cn.gson.financial.kernel.model.entity.FixedAssetChangeRecord;
import cn.gson.financial.kernel.model.entity.FixedAssetDisposalRecord;
import cn.gson.financial.kernel.service.FixedAssetService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/fixed-assets/card")
public class FixedAssetController extends BaseCrudController<FixedAssetService, FixedAssetCard> {
    @PostMapping("{id}/acquire")
    public JsonResult acquire(@PathVariable Integer id, @RequestParam(defaultValue = "true") Boolean generateVoucher) {
        return JsonResult.successful(this.service.acquire(this.accountSetsId, id, this.currentUser, generateVoucher));
    }

    @PostMapping("change")
    public JsonResult change(@RequestBody FixedAssetChangeRecord record, @RequestParam(defaultValue = "true") Boolean generateVoucher) {
        return JsonResult.successful(this.service.change(this.accountSetsId, record, this.currentUser, generateVoucher));
    }

    @PostMapping("dispose")
    public JsonResult dispose(@RequestBody FixedAssetDisposalRecord record, @RequestParam(defaultValue = "true") Boolean generateVoucher) {
        return JsonResult.successful(this.service.dispose(this.accountSetsId, record, this.currentUser, generateVoucher));
    }
}
