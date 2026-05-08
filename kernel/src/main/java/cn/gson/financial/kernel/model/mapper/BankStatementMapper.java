package cn.gson.financial.kernel.model.mapper;

import cn.gson.financial.kernel.model.entity.BankStatement;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface BankStatementMapper extends BaseMapper<BankStatement> {
}
