package cn.gson.financial.kernel.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
@TableName(value = "fxy_financial_payroll_item")
public class PayrollItem implements Serializable {
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @TableField(value = "item_code")
    private String itemCode;

    @TableField(value = "item_name")
    private String itemName;

    @TableField(value = "item_type")
    private String itemType;

    @TableField(value = "formula")
    private String formula;

    @TableField(value = "system_item")
    private Boolean systemItem;

    @TableField(value = "enabled")
    private Boolean enabled;

    @TableField(value = "sort_no")
    private Integer sortNo;

    @TableField(value = "expense_subject_id")
    private Integer expenseSubjectId;

    @TableField(value = "payable_subject_id")
    private Integer payableSubjectId;

    @TableField(value = "remark")
    private String remark;

    @TableField(value = "account_sets_id")
    private Integer accountSetsId;

    private static final long serialVersionUID = 1L;
}
