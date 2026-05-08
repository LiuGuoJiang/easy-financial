package cn.gson.financial.kernel.service;

import cn.gson.financial.kernel.model.entity.PayrollSheet;
import cn.gson.financial.kernel.model.vo.UserVo;

import java.util.List;
import java.util.Map;

public interface PayrollService {
    List<Map<String, Object>> calculate(Integer accountSetsId, Integer year, Integer month);

    PayrollSheet audit(Integer accountSetsId, Integer sheetId, UserVo user);

    PayrollSheet cancelAudit(Integer accountSetsId, Integer sheetId);

    Integer generateVoucher(Integer accountSetsId, Integer sheetId, UserVo user);

    void initDefaultItems(Integer accountSetsId);
}
