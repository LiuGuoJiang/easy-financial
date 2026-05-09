package cn.gson.financial.kernel.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 商业化后台实体：OperationAuditLog
 */
@Data
@TableName(value = "fxy_financial_operation_audit_log")
public class OperationAuditLog implements Serializable {
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 租户 ID
     */
    @TableField(value = "tenant_id")
    private Integer tenantId;

    /**
     * 操作人 ID
     */
    @TableField(value = "operator_id")
    private Integer operatorId;

    /**
     * 操作人
     */
    @TableField(value = "operator_name")
    private String operatorName;

    /**
     * 对象类型
     */
    @TableField(value = "target_type")
    private String targetType;

    /**
     * 对象 ID
     */
    @TableField(value = "target_id")
    private Integer targetId;

    /**
     * 动作
     */
    @TableField(value = "action")
    private String action;

    /**
     * 变更前
     */
    @TableField(value = "before_value")
    private String beforeValue;

    /**
     * 变更后
     */
    @TableField(value = "after_value")
    private String afterValue;

    /**
     * 创建时间
     */
    @TableField(value = "create_date")
    private Date createDate;

    /**
     * IP 地址
     */
    @TableField(value = "ip")
    private String ip;

    private static final long serialVersionUID = 1L;
}
