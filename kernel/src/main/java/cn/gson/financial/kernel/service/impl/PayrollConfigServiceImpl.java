package cn.gson.financial.kernel.service.impl;

import cn.gson.financial.kernel.model.entity.PayrollConfig;
import cn.gson.financial.kernel.model.mapper.PayrollConfigMapper;
import cn.gson.financial.kernel.service.PayrollConfigService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

@Service
public class PayrollConfigServiceImpl extends ServiceImpl<PayrollConfigMapper, PayrollConfig> implements PayrollConfigService {
}
