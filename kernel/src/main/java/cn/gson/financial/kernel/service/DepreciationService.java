package cn.gson.financial.kernel.service;

import cn.gson.financial.kernel.model.entity.DepreciationVoucherRecord;
import cn.gson.financial.kernel.model.vo.UserVo;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

public interface DepreciationService extends IService<DepreciationVoucherRecord> {
    List<DepreciationVoucherRecord> generateMonthly(Integer accountSetsId, Integer year, Integer month, UserVo user, Boolean generateVoucher);

    boolean hasDepreciableAssets(Integer accountSetsId, Integer year, Integer month);

    boolean isDepreciationGenerated(Integer accountSetsId, Integer year, Integer month);
}
