package cn.gson.financial.controller;

import cn.gson.financial.base.BaseCrudController;
import cn.gson.financial.kernel.controller.JsonResult;
import cn.gson.financial.kernel.model.entity.ReceiptBill;
import cn.gson.financial.kernel.service.ReceiptBillService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/fund/receipt")
public class ReceiptBillController extends BaseCrudController<ReceiptBillService, ReceiptBill> {
    @PostMapping("{id}/audit")
    public JsonResult audit(@PathVariable Integer id, @RequestParam(defaultValue = "true") Boolean generateVoucher) {
        this.service.audit(this.accountSetsId, id, this.currentUser, generateVoucher);
        return JsonResult.successful();
    }

    @PostMapping("{id}/cancelAudit")
    public JsonResult cancelAudit(@PathVariable Integer id) {
        this.service.cancelAudit(this.accountSetsId, id);
        return JsonResult.successful();
    }
}
