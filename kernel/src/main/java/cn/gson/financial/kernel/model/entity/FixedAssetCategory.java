package cn.gson.financial.kernel.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
@TableName(value = "fxy_financial_fixed_asset_category")
public class FixedAssetCategory implements Serializable {
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @TableField(value = "code")
    private String code;

    @TableField(value = "name")
    private String name;

    @TableField(value = "depreciation_method")
    private String depreciationMethod;

    @TableField(value = "useful_months")
    private Integer usefulMonths;

    @TableField(value = "net_salvage_rate")
    private Double netSalvageRate;

    @TableField(value = "fixed_asset_subject_id")
    private Integer fixedAssetSubjectId;

    @TableField(value = "accumulated_depreciation_subject_id")
    private Integer accumulatedDepreciationSubjectId;

    @TableField(value = "depreciation_expense_subject_id")
    private Integer depreciationExpenseSubjectId;

    @TableField(value = "status")
    private Boolean status;

    @TableField(value = "remark")
    private String remark;

    @TableField(value = "account_sets_id")
    private Integer accountSetsId;

    @TableField(value = "create_date")
    private Date createDate;

    private static final long serialVersionUID = 1L;
}
