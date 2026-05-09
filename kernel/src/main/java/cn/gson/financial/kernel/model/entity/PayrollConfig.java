package cn.gson.financial.kernel.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
@TableName(value = "fxy_financial_payroll_config")
public class PayrollConfig implements Serializable {
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @TableField(value = "config_type")
    private String configType;

    @TableField(value = "config_name")
    private String configName;

    @TableField(value = "personal_rate")
    private Double personalRate;

    @TableField(value = "company_rate")
    private Double companyRate;

    @TableField(value = "base_amount")
    private Double baseAmount;

    @TableField(value = "threshold_amount")
    private Double thresholdAmount;

    @TableField(value = "tax_brackets")
    private String taxBrackets;

    @TableField(value = "enabled")
    private Boolean enabled;

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
