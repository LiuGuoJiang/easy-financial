package cn.gson.financial.kernel.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 商业化后台实体：Tenant
 */
@Data
@TableName(value = "fxy_financial_tenant")
public class Tenant implements Serializable {
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 商户 ID
     */
    @TableField(value = "merchant_id")
    private Integer merchantId;

    /**
     * 租户名称
     */
    @TableField(value = "name")
    private String name;

    /**
     * 租户编码
     */
    @TableField(value = "code")
    private String code;

    /**
     * 租户管理员用户 ID
     */
    @TableField(value = "admin_user_id")
    private Integer adminUserId;

    /**
     * 状态
     */
    @TableField(value = "status")
    private String status;

    /**
     * 创建时间
     */
    @TableField(value = "create_date")
    private Date createDate;

    /**
     * 到期时间
     */
    @TableField(value = "expire_date")
    private Date expireDate;

    private static final long serialVersionUID = 1L;
}
