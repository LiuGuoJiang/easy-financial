package cn.gson.financial.kernel.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
@TableName(value = "fxy_financial_payment_bill")
public class PaymentBill implements Serializable {
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @TableField(value = "bill_no")
    private String billNo;

    @TableField(value = "bill_date")
    private Date billDate;

    @TableField(value = "account_id")
    private Integer accountId;

    @TableField(value = "category_id")
    private Integer categoryId;

    @TableField(value = "payee")
    private String payee;

    @TableField(value = "amount")
    private Double amount;

    @TableField(value = "summary")
    private String summary;

    @TableField(value = "status")
    private String status;

    @TableField(value = "voucher_id")
    private Integer voucherId;

    @TableField(value = "audit_member_id")
    private Integer auditMemberId;

    @TableField(value = "audit_member_name")
    private String auditMemberName;

    @TableField(value = "audit_date")
    private Date auditDate;

    @TableField(value = "account_sets_id")
    private Integer accountSetsId;

    /**
     * 租户 ID，用于商业化多租户隔离
     */
    @TableField(value = "tenant_id")
    private Integer tenantId;

    private static final long serialVersionUID = 1L;
}
