package cn.gson.financial.kernel.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
@TableName(value = "fxy_financial_fund_account")
public class FundAccount implements Serializable {
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @TableField(value = "code")
    private String code;

    @TableField(value = "name")
    private String name;

    @TableField(value = "account_type")
    private String accountType;

    @TableField(value = "bank_name")
    private String bankName;

    @TableField(value = "bank_account_no")
    private String bankAccountNo;

    @TableField(value = "subject_id")
    private Integer subjectId;

    @TableField(value = "opening_balance")
    private Double openingBalance;

    @TableField(value = "current_balance")
    private Double currentBalance;

    @TableField(value = "status")
    private Boolean status;

    @TableField(value = "remark")
    private String remark;

    @TableField(value = "account_sets_id")
    private Integer accountSetsId;

    /**
     * 租户 ID，用于商业化多租户隔离
     */
    @TableField(value = "tenant_id")
    private Integer tenantId;

    @TableField(value = "create_date")
    private Date createDate;

    private static final long serialVersionUID = 1L;
}
