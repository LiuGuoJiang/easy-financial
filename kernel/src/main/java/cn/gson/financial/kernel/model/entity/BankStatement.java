package cn.gson.financial.kernel.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
@TableName(value = "fxy_financial_bank_statement")
public class BankStatement implements Serializable {
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @TableField(value = "statement_no")
    private String statementNo;

    @TableField(value = "statement_date")
    private Date statementDate;

    @TableField(value = "account_id")
    private Integer accountId;

    @TableField(value = "direction")
    private String direction;

    @TableField(value = "amount")
    private Double amount;

    @TableField(value = "balance")
    private Double balance;

    @TableField(value = "counterparty")
    private String counterparty;

    @TableField(value = "summary")
    private String summary;

    @TableField(value = "fund_flow_id")
    private Integer fundFlowId;

    @TableField(value = "reconcile_status")
    private String reconcileStatus;

    @TableField(value = "account_sets_id")
    private Integer accountSetsId;

    /**
     * 租户 ID，用于商业化多租户隔离
     */
    @TableField(value = "tenant_id")
    private Integer tenantId;

    private static final long serialVersionUID = 1L;
}
