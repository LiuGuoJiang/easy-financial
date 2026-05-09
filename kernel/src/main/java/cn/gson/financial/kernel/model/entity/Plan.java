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
 * 商业化后台实体：Plan
 */
@Data
@TableName(value = "fxy_financial_plan")
public class Plan implements Serializable {
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 套餐名称
     */
    @TableField(value = "name")
    private String name;

    /**
     * 套餐编码
     */
    @TableField(value = "code")
    private String code;

    /**
     * 套餐价格
     */
    @TableField(value = "price")
    private BigDecimal price;

    /**
     * 计费周期
     */
    @TableField(value = "billing_cycle")
    private String billingCycle;

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

    /**
     * 状态
     */
    @TableField(value = "status")
    private String status;

    /**
     * 套餐说明
     */
    @TableField(value = "description")
    private String description;

    private static final long serialVersionUID = 1L;
}
