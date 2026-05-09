package cn.gson.financial.kernel.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 商业化后台实体：Subscription
 */
@Data
@TableName(value = "fxy_financial_subscription")
public class Subscription implements Serializable {
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 商户 ID
     */
    @TableField(value = "merchant_id")
    private Integer merchantId;

    /**
     * 租户 ID
     */
    @TableField(value = "tenant_id")
    private Integer tenantId;

    /**
     * 套餐 ID
     */
    @TableField(value = "plan_id")
    private Integer planId;

    /**
     * 状态
     */
    @TableField(value = "status")
    private String status;

    /**
     * 开始时间
     */
    @TableField(value = "start_date")
    private Date startDate;

    /**
     * 结束时间
     */
    @TableField(value = "end_date")
    private Date endDate;

    /**
     * 账套数量限制
     */
    @TableField(value = "account_set_limit")
    private Integer accountSetLimit;

    /**
     * 用户数量限制
     */
    @TableField(value = "user_limit")
    private Integer userLimit;

    /**
     * 凭证/授权额度
     */
    @TableField(value = "voucher_limit")
    private Integer voucherLimit;

    private static final long serialVersionUID = 1L;
}
