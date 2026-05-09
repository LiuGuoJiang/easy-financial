package cn.gson.financial.kernel.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.math.BigDecimal;

/**
 * 商业化后台实体：MerchantOrder
 */
@Data
@TableName(value = "fxy_financial_order")
public class MerchantOrder implements Serializable {
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
     * 订阅 ID
     */
    @TableField(value = "subscription_id")
    private Integer subscriptionId;

    /**
     * 订单号
     */
    @TableField(value = "order_no")
    private String orderNo;

    /**
     * 订单金额
     */
    @TableField(value = "amount")
    private BigDecimal amount;

    /**
     * 订单状态
     */
    @TableField(value = "status")
    private String status;

    /**
     * 支付时间
     */
    @TableField(value = "pay_time")
    private Date payTime;

    /**
     * 创建时间
     */
    @TableField(value = "create_date")
    private Date createDate;

    private static final long serialVersionUID = 1L;
}
