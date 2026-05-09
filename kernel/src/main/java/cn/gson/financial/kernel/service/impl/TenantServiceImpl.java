package cn.gson.financial.kernel.service.impl;

import cn.gson.financial.kernel.model.entity.Tenant;
import cn.gson.financial.kernel.model.mapper.TenantMapper;
import cn.gson.financial.kernel.service.TenantService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

@Service
public class TenantServiceImpl extends ServiceImpl<TenantMapper, Tenant> implements TenantService {
}
