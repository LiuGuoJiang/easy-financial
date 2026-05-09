package cn.gson.financial.kernel.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
@TableName(value = "fxy_financial_fund_flow")
public class FundFlow implements Serializable {
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @TableField(value = "flow_no")
    private String flowNo;

    @TableField(value = "flow_date")
    private Date flowDate;

    @TableField(value = "account_id")
    private Integer accountId;

    @TableField(value = "category_id")
    private Integer categoryId;

    @TableField(value = "direction")
    private String direction;

    @TableField(value = "amount")
    private Double amount;

    @TableField(value = "balance")
    private Double balance;

    @TableField(value = "source_type")
    private String sourceType;

    @TableField(value = "source_id")
    private Integer sourceId;

    @TableField(value = "voucher_id")
    private Integer voucherId;

    @TableField(value = "counterparty")
    private String counterparty;

    @TableField(value = "summary")
    private String summary;

    @TableField(value = "reconciled")
    private Boolean reconciled;

    @TableField(value = "account_sets_id")
    private Integer accountSetsId;

    /**
     * 租户 ID，用于商业化多租户隔离
     */
    @TableField(value = "tenant_id")
    private Integer tenantId;

    @TableField(exist = false)
    private FundAccount account;

    @TableField(exist = false)
    private FundCategory category;

    private static final long serialVersionUID = 1L;
}
