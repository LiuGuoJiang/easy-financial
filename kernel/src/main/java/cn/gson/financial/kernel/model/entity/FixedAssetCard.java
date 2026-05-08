package cn.gson.financial.kernel.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
@TableName(value = "fxy_financial_fixed_asset_card")
public class FixedAssetCard implements Serializable {
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @TableField(value = "asset_no")
    private String assetNo;

    @TableField(value = "asset_name")
    private String assetName;

    @TableField(value = "category_id")
    private Integer categoryId;

    @TableField(value = "specification")
    private String specification;

    @TableField(value = "department")
    private String department;

    @TableField(value = "custodian")
    private String custodian;

    @TableField(value = "purchase_date")
    private Date purchaseDate;

    @TableField(value = "start_use_date")
    private Date startUseDate;

    @TableField(value = "original_value")
    private Double originalValue;

    @TableField(value = "net_salvage_rate")
    private Double netSalvageRate;

    @TableField(value = "expected_salvage")
    private Double expectedSalvage;

    @TableField(value = "useful_months")
    private Integer usefulMonths;

    @TableField(value = "depreciation_method")
    private String depreciationMethod;

    @TableField(value = "depreciated_months")
    private Integer depreciatedMonths;

    @TableField(value = "accumulated_depreciation")
    private Double accumulatedDepreciation;

    @TableField(value = "net_value")
    private Double netValue;

    @TableField(value = "fixed_asset_subject_id")
    private Integer fixedAssetSubjectId;

    @TableField(value = "accumulated_depreciation_subject_id")
    private Integer accumulatedDepreciationSubjectId;

    @TableField(value = "depreciation_expense_subject_id")
    private Integer depreciationExpenseSubjectId;

    @TableField(value = "status")
    private String status;

    @TableField(value = "voucher_id")
    private Integer voucherId;

    @TableField(value = "remark")
    private String remark;

    @TableField(value = "account_sets_id")
    private Integer accountSetsId;

    @TableField(value = "create_date")
    private Date createDate;

    private static final long serialVersionUID = 1L;
}
