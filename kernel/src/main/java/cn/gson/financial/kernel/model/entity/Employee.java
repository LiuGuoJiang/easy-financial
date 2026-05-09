package cn.gson.financial.kernel.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
@TableName(value = "fxy_financial_employee")
public class Employee implements Serializable {
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @TableField(value = "employee_no")
    private String employeeNo;

    @TableField(value = "employee_name")
    private String employeeName;

    @TableField(value = "department_id")
    private Integer departmentId;

    @TableField(value = "department_name")
    private String departmentName;

    @TableField(value = "gender")
    private String gender;

    @TableField(value = "id_card_no")
    private String idCardNo;

    @TableField(value = "phone")
    private String phone;

    @TableField(value = "bank_name")
    private String bankName;

    @TableField(value = "bank_account")
    private String bankAccount;

    @TableField(value = "entry_date")
    private Date entryDate;

    @TableField(value = "leave_date")
    private Date leaveDate;

    @TableField(value = "status")
    private String status;

    @TableField(value = "base_salary")
    private Double baseSalary;

    @TableField(value = "expense_subject_id")
    private Integer expenseSubjectId;

    @TableField(value = "remark")
    private String remark;

    @TableField(value = "account_sets_id")
    private Integer accountSetsId;

    /**
     * 租户 ID，用于商业化多租户隔离
     */
    @TableField(value = "tenant_id")
    private Integer tenantId;

    private static final long serialVersionUID = 1L;
}
