package cn.gson.financial.kernel.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
@TableName(value = "fxy_financial_payroll_department")
public class PayrollDepartment implements Serializable {
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @TableField(value = "dept_code")
    private String deptCode;

    @TableField(value = "dept_name")
    private String deptName;

    @TableField(value = "parent_id")
    private Integer parentId;

    @TableField(value = "manager_employee_id")
    private Integer managerEmployeeId;

    @TableField(value = "remark")
    private String remark;

    @TableField(value = "enabled")
    private Boolean enabled;

    @TableField(value = "account_sets_id")
    private Integer accountSetsId;

    private static final long serialVersionUID = 1L;
}
