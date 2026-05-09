package cn.gson.financial.kernel.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
@TableName(value = "fxy_financial_payroll_detail")
public class PayrollDetail implements Serializable {
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @TableField(value = "sheet_id")
    private Integer sheetId;

    @TableField(value = "employee_id")
    private Integer employeeId;

    @TableField(value = "employee_no")
    private String employeeNo;

    @TableField(value = "employee_name")
    private String employeeName;

    @TableField(value = "department_id")
    private Integer departmentId;

    @TableField(value = "department_name")
    private String departmentName;

    @TableField(value = "item_id")
    private Integer itemId;

    @TableField(value = "item_code")
    private String itemCode;

    @TableField(value = "item_name")
    private String itemName;

    @TableField(value = "item_type")
    private String itemType;

    @TableField(value = "amount")
    private Double amount;

    @TableField(value = "expense_subject_id")
    private Integer expenseSubjectId;

    @TableField(value = "account_sets_id")
    private Integer accountSetsId;

    /**
     * 租户 ID，用于商业化多租户隔离
     */
    @TableField(value = "tenant_id")
    private Integer tenantId;

    private static final long serialVersionUID = 1L;
}
