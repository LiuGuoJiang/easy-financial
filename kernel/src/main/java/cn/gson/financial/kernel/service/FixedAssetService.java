package cn.gson.financial.kernel.service;

import cn.gson.financial.kernel.model.entity.FixedAssetCard;
import cn.gson.financial.kernel.model.entity.FixedAssetChangeRecord;
import cn.gson.financial.kernel.model.entity.FixedAssetDisposalRecord;
import cn.gson.financial.kernel.model.vo.UserVo;
import com.baomidou.mybatisplus.extension.service.IService;

public interface FixedAssetService extends IService<FixedAssetCard> {
    Integer acquire(Integer accountSetsId, Integer id, UserVo user, Boolean generateVoucher);

    Integer change(Integer accountSetsId, FixedAssetChangeRecord record, UserVo user, Boolean generateVoucher);

    Integer dispose(Integer accountSetsId, FixedAssetDisposalRecord record, UserVo user, Boolean generateVoucher);
}
