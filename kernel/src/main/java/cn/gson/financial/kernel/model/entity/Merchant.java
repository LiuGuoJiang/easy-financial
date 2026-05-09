package cn.gson.financial.kernel.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 商业化后台实体：Merchant
 */
@Data
@TableName(value = "fxy_financial_merchant")
public class Merchant implements Serializable {
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 商户名称
     */
    @TableField(value = "name")
    private String name;

    /**
     * 商户编码
     */
    @TableField(value = "code")
    private String code;

    /**
     * 联系人
     */
    @TableField(value = "contact_name")
    private String contactName;

    /**
     * 联系电话
     */
    @TableField(value = "contact_mobile")
    private String contactMobile;

    /**
     * 状态
     */
    @TableField(value = "status")
    private String status;

    /**
     * 默认租户 ID
     */
    @TableField(value = "tenant_id")
    private Integer tenantId;

    /**
     * 创建时间
     */
    @TableField(value = "create_date")
    private Date createDate;

    /**
     * 备注
     */
    @TableField(value = "remark")
    private String remark;

    private static final long serialVersionUID = 1L;
}
