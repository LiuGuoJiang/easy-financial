package cn.gson.financial.kernel.service;

import cn.gson.financial.kernel.model.entity.FundFlow;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.Map;

public interface FundFlowService extends IService<FundFlow> {
    Map<String, Object> report(Integer accountSetsId);
}
