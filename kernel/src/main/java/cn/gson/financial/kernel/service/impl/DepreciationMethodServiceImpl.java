package cn.gson.financial.kernel.service.impl;

import cn.gson.financial.kernel.model.entity.DepreciationMethod;
import cn.gson.financial.kernel.model.mapper.DepreciationMethodMapper;
import cn.gson.financial.kernel.service.DepreciationMethodService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

@Service
public class DepreciationMethodServiceImpl extends ServiceImpl<DepreciationMethodMapper, DepreciationMethod> implements DepreciationMethodService {
}
