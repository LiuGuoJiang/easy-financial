package cn.gson.financial.kernel.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
@TableName(value = "fxy_financial_fixed_asset_disposal")
public class FixedAssetDisposalRecord implements Serializable {
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @TableField(value = "asset_id")
    private Integer assetId;

    @TableField(value = "disposal_date")
    private Date disposalDate;

    @TableField(value = "disposal_type")
    private String disposalType;

    @TableField(value = "disposal_income")
    private Double disposalIncome;

    @TableField(value = "disposal_expense")
    private Double disposalExpense;

    @TableField(value = "net_value")
    private Double netValue;

    @TableField(value = "gain_loss")
    private Double gainLoss;

    @TableField(value = "summary")
    private String summary;

    @TableField(value = "voucher_id")
    private Integer voucherId;

    @TableField(value = "account_sets_id")
    private Integer accountSetsId;

    private static final long serialVersionUID = 1L;
}
