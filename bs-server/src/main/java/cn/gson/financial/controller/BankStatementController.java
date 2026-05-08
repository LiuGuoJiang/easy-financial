package cn.gson.financial.controller;

import cn.gson.financial.base.BaseCrudController;
import cn.gson.financial.kernel.controller.JsonResult;
import cn.gson.financial.kernel.model.entity.BankStatement;
import cn.gson.financial.kernel.service.BankStatementService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/fund/bank-statement")
public class BankStatementController extends BaseCrudController<BankStatementService, BankStatement> {
    @PostMapping("{id}/reconcile")
    public JsonResult reconcile(@PathVariable Integer id, Integer flowId) {
        this.service.reconcile(this.accountSetsId, id, flowId);
        return JsonResult.successful();
    }

    @PostMapping("{id}/cancelReconcile")
    public JsonResult cancelReconcile(@PathVariable Integer id) {
        this.service.cancelReconcile(this.accountSetsId, id);
        return JsonResult.successful();
    }
}
