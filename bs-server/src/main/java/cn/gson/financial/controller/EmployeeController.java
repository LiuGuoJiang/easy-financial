package cn.gson.financial.controller;

import cn.gson.financial.base.BaseCrudController;
import cn.gson.financial.kernel.controller.JsonResult;
import cn.gson.financial.kernel.model.entity.Employee;
import cn.gson.financial.kernel.model.entity.PayrollDepartment;
import cn.gson.financial.kernel.service.EmployeeService;
import cn.gson.financial.kernel.service.PayrollDepartmentService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/payroll/employee")
@AllArgsConstructor
public class EmployeeController extends BaseCrudController<EmployeeService, Employee> {
    private PayrollDepartmentService departmentService;

    @GetMapping("/departments")
    public JsonResult departments() {
        QueryWrapper<PayrollDepartment> qw = Wrappers.query();
        qw.eq("account_sets_id", accountSetsId).orderByAsc("dept_code");
        return JsonResult.successful(departmentService.list(qw));
    }

    @PostMapping("/departments")
    public JsonResult saveDepartment(@RequestBody PayrollDepartment department) {
        department.setAccountSetsId(accountSetsId);
        departmentService.save(department);
        return JsonResult.successful();
    }

    @PutMapping("/departments")
    public JsonResult updateDepartment(@RequestBody PayrollDepartment department) {
        department.setAccountSetsId(accountSetsId);
        QueryWrapper<PayrollDepartment> qw = Wrappers.query();
        qw.eq("id", department.getId()).eq("account_sets_id", accountSetsId);
        departmentService.update(department, qw);
        return JsonResult.successful();
    }

    @DeleteMapping("/departments/{id:\\d+}")
    public JsonResult deleteDepartment(@PathVariable Integer id) {
        QueryWrapper<PayrollDepartment> qw = Wrappers.query();
        qw.eq("id", id).eq("account_sets_id", accountSetsId);
        departmentService.remove(qw);
        return JsonResult.successful();
    }
}
