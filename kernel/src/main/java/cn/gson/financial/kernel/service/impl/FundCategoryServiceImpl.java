package cn.gson.financial.kernel.service.impl;

import cn.gson.financial.kernel.model.entity.FundCategory;
import cn.gson.financial.kernel.model.mapper.FundCategoryMapper;
import cn.gson.financial.kernel.service.FundCategoryService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

@Service
public class FundCategoryServiceImpl extends ServiceImpl<FundCategoryMapper, FundCategory> implements FundCategoryService {
    @Override
    public boolean save(FundCategory entity) {
        if (entity.getStatus() == null) {
            entity.setStatus(true);
        }
        return super.save(entity);
    }
}
