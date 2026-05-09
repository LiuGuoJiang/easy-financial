package cn.gson.financial.kernel.service.impl;

import cn.gson.financial.kernel.model.entity.AuthorizationQuota;
import cn.gson.financial.kernel.model.mapper.AuthorizationQuotaMapper;
import cn.gson.financial.kernel.service.AuthorizationQuotaService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

@Service
public class AuthorizationQuotaServiceImpl extends ServiceImpl<AuthorizationQuotaMapper, AuthorizationQuota> implements AuthorizationQuotaService {
}
