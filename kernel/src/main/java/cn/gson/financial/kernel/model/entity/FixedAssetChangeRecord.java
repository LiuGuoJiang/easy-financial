package cn.gson.financial.kernel.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
@TableName(value = "fxy_financial_fixed_asset_change")
public class FixedAssetChangeRecord implements Serializable {
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @TableField(value = "asset_id")
    private Integer assetId;

    @TableField(value = "change_date")
    private Date changeDate;

    @TableField(value = "change_type")
    private String changeType;

    @TableField(value = "before_value")
    private Double beforeValue;

    @TableField(value = "after_value")
    private Double afterValue;

    @TableField(value = "summary")
    private String summary;

    @TableField(value = "voucher_id")
    private Integer voucherId;

    @TableField(value = "account_sets_id")
    private Integer accountSetsId;

    /**
     * 租户 ID，用于商业化多租户隔离
     */
    @TableField(value = "tenant_id")
    private Integer tenantId;

    private static final long serialVersionUID = 1L;
}
