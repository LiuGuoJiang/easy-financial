package cn.gson.financial.kernel.service.impl;

import cn.gson.financial.kernel.model.entity.FundFlow;
import cn.gson.financial.kernel.model.mapper.FundAccountMapper;
import cn.gson.financial.kernel.model.mapper.FundFlowMapper;
import cn.gson.financial.kernel.service.FundFlowService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@AllArgsConstructor
public class FundFlowServiceImpl extends ServiceImpl<FundFlowMapper, FundFlow> implements FundFlowService {
    private FundAccountMapper fundAccountMapper;

    @Override
    public Map<String, Object> report(Integer accountSetsId) {
        LambdaQueryWrapper<FundFlow> qw = Wrappers.lambdaQuery();
        qw.eq(FundFlow::getAccountSetsId, accountSetsId);
        List<FundFlow> flows = this.list(qw);
        double income = flows.stream().filter(f -> "IN".equals(f.getDirection())).mapToDouble(f -> f.getAmount() == null ? 0d : f.getAmount()).sum();
        double expense = flows.stream().filter(f -> "OUT".equals(f.getDirection())).mapToDouble(f -> f.getAmount() == null ? 0d : f.getAmount()).sum();
        Map<String, Object> report = new HashMap<>(4);
        report.put("income", income);
        report.put("expense", expense);
        report.put("net", income - expense);
        report.put("accounts", fundAccountMapper.selectList(Wrappers.<cn.gson.financial.kernel.model.entity.FundAccount>lambdaQuery().eq(cn.gson.financial.kernel.model.entity.FundAccount::getAccountSetsId, accountSetsId)));
        return report;
    }
}
