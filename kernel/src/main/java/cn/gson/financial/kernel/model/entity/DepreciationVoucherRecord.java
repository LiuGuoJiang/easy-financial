package cn.gson.financial.kernel.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
@TableName(value = "fxy_financial_fixed_asset_depreciation")
public class DepreciationVoucherRecord implements Serializable {
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @TableField(value = "asset_id")
    private Integer assetId;

    @TableField(value = "depreciation_year")
    private Integer depreciationYear;

    @TableField(value = "depreciation_month")
    private Integer depreciationMonth;

    @TableField(value = "depreciation_date")
    private Date depreciationDate;

    @TableField(value = "amount")
    private Double amount;

    @TableField(value = "before_accumulated_depreciation")
    private Double beforeAccumulatedDepreciation;

    @TableField(value = "after_accumulated_depreciation")
    private Double afterAccumulatedDepreciation;

    @TableField(value = "voucher_id")
    private Integer voucherId;

    @TableField(value = "account_sets_id")
    private Integer accountSetsId;

    private static final long serialVersionUID = 1L;
}
