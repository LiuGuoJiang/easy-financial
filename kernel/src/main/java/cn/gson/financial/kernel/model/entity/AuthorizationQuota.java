package cn.gson.financial.kernel.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 商业化后台实体：AuthorizationQuota
 */
@Data
@TableName(value = "fxy_financial_authorization_quota")
public class AuthorizationQuota implements Serializable {
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
     * 额度类型
     */
    @TableField(value = "quota_type")
    private String quotaType;

    /**
     * 额度上限
     */
    @TableField(value = "quota_limit")
    private Integer quotaLimit;

    /**
     * 已用额度
     */
    @TableField(value = "used_quota")
    private Integer usedQuota;

    /**
     * 到期时间
     */
    @TableField(value = "expire_date")
    private Date expireDate;

    private static final long serialVersionUID = 1L;
}
