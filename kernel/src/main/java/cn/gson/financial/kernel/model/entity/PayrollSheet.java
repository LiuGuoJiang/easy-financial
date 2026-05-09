package cn.gson.financial.kernel.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
@TableName(value = "fxy_financial_payroll_sheet")
public class PayrollSheet implements Serializable {
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @TableField(value = "period_id")
    private Integer periodId;

    @TableField(value = "payroll_year")
    private Integer payrollYear;

    @TableField(value = "payroll_month")
    private Integer payrollMonth;

    @TableField(value = "sheet_no")
    private String sheetNo;

    @TableField(value = "sheet_name")
    private String sheetName;

    @TableField(value = "status")
    private String status;

    @TableField(value = "gross_amount")
    private Double grossAmount;

    @TableField(value = "deduction_amount")
    private Double deductionAmount;

    @TableField(value = "net_amount")
    private Double netAmount;

    @TableField(value = "voucher_id")
    private Integer voucherId;

    @TableField(value = "audit_member_id")
    private Integer auditMemberId;

    @TableField(value = "audit_member_name")
    private String auditMemberName;

    @TableField(value = "audit_date")
    private Date auditDate;

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
