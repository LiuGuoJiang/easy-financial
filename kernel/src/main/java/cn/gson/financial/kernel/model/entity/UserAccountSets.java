package cn.gson.financial.kernel.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import lombok.Data;

@Data
@TableName(value = "fxy_financial_user_account_sets")
public class UserAccountSets implements Serializable {
    @TableField(value = "account_sets_id")
    private Integer accountSetsId;

    /**
     * 租户 ID，用于商业化多租户隔离
     */
    @TableField(value = "tenant_id")
    private Integer tenantId;

    @TableField(value = "user_id")
    private Integer userId;

    /**
     * 账套角色
     */
    @TableField(value = "role_type")
    private String roleType;

    private static final long serialVersionUID = 1L;

    public static final String COL_ACCOUNT_SETS_ID = "account_sets_id";

    public static final String COL_TENANT_ID = "tenant_id";

    public static final String COL_USER_ID = "user_id";

    public static final String COL_ROLE_TYPE = "role_type";
}