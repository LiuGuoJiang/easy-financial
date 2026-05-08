package cn.gson.financial.kernel.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

@Data
@TableName(value = "fxy_financial_depreciation_method")
public class DepreciationMethod implements Serializable {
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @TableField(value = "code")
    private String code;

    @TableField(value = "name")
    private String name;

    @TableField(value = "formula_type")
    private String formulaType;

    @TableField(value = "description")
    private String description;

    @TableField(value = "system_default")
    private Boolean systemDefault;

    @TableField(value = "account_sets_id")
    private Integer accountSetsId;

    private static final long serialVersionUID = 1L;
}
