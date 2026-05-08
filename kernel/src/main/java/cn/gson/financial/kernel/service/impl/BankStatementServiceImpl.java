package cn.gson.financial.kernel.service.impl;

import cn.gson.financial.kernel.exception.ServiceException;
import cn.gson.financial.kernel.model.entity.BankStatement;
import cn.gson.financial.kernel.model.entity.FundFlow;
import cn.gson.financial.kernel.model.mapper.BankStatementMapper;
import cn.gson.financial.kernel.model.mapper.FundFlowMapper;
import cn.gson.financial.kernel.service.BankStatementService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
public class BankStatementServiceImpl extends ServiceImpl<BankStatementMapper, BankStatement> implements BankStatementService {
    private FundFlowMapper fundFlowMapper;

    @Override
    public boolean save(BankStatement entity) {
        if (entity.getReconcileStatus() == null) {
            entity.setReconcileStatus("UNRECONCILED");
        }
        return super.save(entity);
    }

    @Override
    @Transactional
    public void reconcile(Integer accountSetsId, Integer statementId, Integer flowId) {
        BankStatement statement = getStatement(accountSetsId, statementId);
        FundFlow flow = fundFlowMapper.selectById(flowId);
        if (flow == null || !accountSetsId.equals(flow.getAccountSetsId())) {
            throw new ServiceException("资金流水不存在！");
        }
        if (!statement.getAccountId().equals(flow.getAccountId())) {
            throw new ServiceException("银行对账单与资金流水账户不一致！");
        }
        statement.setFundFlowId(flowId);
        statement.setReconcileStatus("RECONCILED");
        flow.setReconciled(true);
        baseMapper.updateById(statement);
        fundFlowMapper.updateById(flow);
    }

    @Override
    @Transactional
    public void cancelReconcile(Integer accountSetsId, Integer statementId) {
        BankStatement statement = getStatement(accountSetsId, statementId);
        if (statement.getFundFlowId() != null) {
            FundFlow flow = fundFlowMapper.selectById(statement.getFundFlowId());
            if (flow != null) {
                flow.setReconciled(false);
                fundFlowMapper.updateById(flow);
            }
        }
        statement.setFundFlowId(null);
        statement.setReconcileStatus("UNRECONCILED");
        baseMapper.updateById(statement);
    }

    private BankStatement getStatement(Integer accountSetsId, Integer statementId) {
        BankStatement statement = baseMapper.selectById(statementId);
        if (statement == null || !accountSetsId.equals(statement.getAccountSetsId())) {
            throw new ServiceException("银行对账单不存在！");
        }
        return statement;
    }
}
