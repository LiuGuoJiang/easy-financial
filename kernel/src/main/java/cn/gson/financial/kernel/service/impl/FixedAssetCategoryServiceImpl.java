package cn.gson.financial.kernel.service.impl;

import cn.gson.financial.kernel.model.entity.FixedAssetCategory;
import cn.gson.financial.kernel.model.mapper.FixedAssetCategoryMapper;
import cn.gson.financial.kernel.service.FixedAssetCategoryService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class FixedAssetCategoryServiceImpl extends ServiceImpl<FixedAssetCategoryMapper, FixedAssetCategory> implements FixedAssetCategoryService {
    @Override
    public boolean save(FixedAssetCategory entity) {
        if (entity.getStatus() == null) {
            entity.setStatus(true);
        }
        if (entity.getCreateDate() == null) {
            entity.setCreateDate(new Date());
        }
        return super.save(entity);
    }
}
