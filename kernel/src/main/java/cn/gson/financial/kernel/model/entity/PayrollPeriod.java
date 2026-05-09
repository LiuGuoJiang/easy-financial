package cn.gson.financial.kernel.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
@TableName(value = "fxy_financial_payroll_period")
public class PayrollPeriod implements Serializable {
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @TableField(value = "payroll_year")
    private Integer payrollYear;

    @TableField(value = "payroll_month")
    private Integer payrollMonth;

    @TableField(value = "begin_date")
    private Date beginDate;

    @TableField(value = "end_date")
    private Date endDate;

    @TableField(value = "status")
    private String status;

    @TableField(value = "account_sets_id")
    private Integer accountSetsId;

    /**
     * 租户 ID，用于商业化多租户隔离
     */
    @TableField(value = "tenant_id")
    private Integer tenantId;

    private static final long serialVersionUID = 1L;
}
