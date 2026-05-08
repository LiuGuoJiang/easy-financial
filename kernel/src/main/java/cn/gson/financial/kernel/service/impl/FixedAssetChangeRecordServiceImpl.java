package cn.gson.financial.kernel.service.impl;

import cn.gson.financial.kernel.model.entity.FixedAssetChangeRecord;
import cn.gson.financial.kernel.model.mapper.FixedAssetChangeRecordMapper;
import cn.gson.financial.kernel.service.FixedAssetChangeRecordService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

@Service
public class FixedAssetChangeRecordServiceImpl extends ServiceImpl<FixedAssetChangeRecordMapper, FixedAssetChangeRecord> implements FixedAssetChangeRecordService {
}
