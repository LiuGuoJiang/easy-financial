package cn.gson.financial.kernel.service;

import cn.gson.financial.kernel.model.entity.BankStatement;
import com.baomidou.mybatisplus.extension.service.IService;

public interface BankStatementService extends IService<BankStatement> {
    void reconcile(Integer accountSetsId, Integer statementId, Integer flowId);

    void cancelReconcile(Integer accountSetsId, Integer statementId);
}
