package cn.gson.financial.controller;

import cn.gson.financial.base.BaseController;
import cn.gson.financial.kernel.controller.JsonResult;
import cn.gson.financial.kernel.model.entity.PayrollConfig;
import cn.gson.financial.kernel.model.entity.PayrollItem;
import cn.gson.financial.kernel.model.entity.PayrollSheet;
import cn.gson.financial.kernel.service.PayrollConfigService;
import cn.gson.financial.kernel.service.PayrollItemService;
import cn.gson.financial.kernel.service.PayrollService;
import cn.gson.financial.kernel.service.PayrollSheetService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/payroll")
@AllArgsConstructor
public class PayrollController extends BaseController {
    private PayrollService payrollService;
    private PayrollItemService payrollItemService;
    private PayrollConfigService payrollConfigService;
    private PayrollSheetService payrollSheetService;

    @PostMapping("/items/init")
    public JsonResult initItems() {
        payrollService.initDefaultItems(accountSetsId);
        return JsonResult.successful();
    }

    @GetMapping("/items")
    public JsonResult items() {
        QueryWrapper<PayrollItem> qw = Wrappers.query();
        qw.eq("account_sets_id", accountSetsId).orderByAsc("sort_no").orderByAsc("id");
        return JsonResult.successful(payrollItemService.list(qw));
    }

    @PostMapping("/items")
    public JsonResult saveItem(@RequestBody PayrollItem item) {
        item.setAccountSetsId(accountSetsId);
        payrollItemService.save(item);
        return JsonResult.successful();
    }

    @PutMapping("/items")
    public JsonResult updateItem(@RequestBody PayrollItem item) {
        item.setAccountSetsId(accountSetsId);
        QueryWrapper<PayrollItem> qw = Wrappers.query();
        qw.eq("id", item.getId()).eq("account_sets_id", accountSetsId);
        payrollItemService.update(item, qw);
        return JsonResult.successful();
    }

    @DeleteMapping("/items/{id:\\d+}")
    public JsonResult deleteItem(@PathVariable Integer id) {
        QueryWrapper<PayrollItem> qw = Wrappers.query();
        qw.eq("id", id).eq("account_sets_id", accountSetsId);
        payrollItemService.remove(qw);
        return JsonResult.successful();
    }

    @GetMapping("/configs")
    public JsonResult configs() {
        QueryWrapper<PayrollConfig> qw = Wrappers.query();
        qw.eq("account_sets_id", accountSetsId).orderByAsc("config_type").orderByAsc("id");
        return JsonResult.successful(payrollConfigService.list(qw));
    }

    @PostMapping("/configs")
    public JsonResult saveConfig(@RequestBody PayrollConfig config) {
        config.setAccountSetsId(accountSetsId);
        payrollConfigService.save(config);
        return JsonResult.successful();
    }

    @PutMapping("/configs")
    public JsonResult updateConfig(@RequestBody PayrollConfig config) {
        config.setAccountSetsId(accountSetsId);
        QueryWrapper<PayrollConfig> qw = Wrappers.query();
        qw.eq("id", config.getId()).eq("account_sets_id", accountSetsId);
        payrollConfigService.update(config, qw);
        return JsonResult.successful();
    }

    @DeleteMapping("/configs/{id:\\d+}")
    public JsonResult deleteConfig(@PathVariable Integer id) {
        QueryWrapper<PayrollConfig> qw = Wrappers.query();
        qw.eq("id", id).eq("account_sets_id", accountSetsId);
        payrollConfigService.remove(qw);
        return JsonResult.successful();
    }

    @GetMapping("/sheets")
    public JsonResult sheets(@RequestParam Map<String, String> params) {
        QueryWrapper<PayrollSheet> qw = Wrappers.query();
        qw.eq("account_sets_id", accountSetsId);
        if (params.containsKey("payrollYear")) qw.eq("payroll_year", params.get("payrollYear"));
        if (params.containsKey("payrollMonth")) qw.eq("payroll_month", params.get("payrollMonth"));
        qw.orderByDesc("payroll_year").orderByDesc("payroll_month");
        return JsonResult.successful(payrollSheetService.list(qw));
    }

    @PostMapping("/calculate")
    public JsonResult calculate(@RequestParam Integer year, @RequestParam Integer month) {
        return JsonResult.successful(payrollService.calculate(accountSetsId, year, month));
    }

    @PostMapping("/sheets/{id:\\d+}/audit")
    public JsonResult audit(@PathVariable Integer id) {
        return JsonResult.successful(payrollService.audit(accountSetsId, id, currentUser));
    }

    @PostMapping("/sheets/{id:\\d+}/cancelAudit")
    public JsonResult cancelAudit(@PathVariable Integer id) {
        return JsonResult.successful(payrollService.cancelAudit(accountSetsId, id));
    }

    @PostMapping("/sheets/{id:\\d+}/voucher")
    public JsonResult generateVoucher(@PathVariable Integer id) {
        return JsonResult.successful(payrollService.generateVoucher(accountSetsId, id, currentUser));
    }
}
