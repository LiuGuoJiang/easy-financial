package cn.gson.financial.controller;

import cn.gson.financial.base.BaseCrudController;
import cn.gson.financial.kernel.controller.JsonResult;
import cn.gson.financial.kernel.model.entity.PaymentBill;
import cn.gson.financial.kernel.service.PaymentBillService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/fund/payment")
public class PaymentBillController extends BaseCrudController<PaymentBillService, PaymentBill> {
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
