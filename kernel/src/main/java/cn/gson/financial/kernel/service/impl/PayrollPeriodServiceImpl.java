package cn.gson.financial.kernel.service.impl;

import cn.gson.financial.kernel.model.entity.PayrollPeriod;
import cn.gson.financial.kernel.model.mapper.PayrollPeriodMapper;
import cn.gson.financial.kernel.service.PayrollPeriodService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

@Service
public class PayrollPeriodServiceImpl extends ServiceImpl<PayrollPeriodMapper, PayrollPeriod> implements PayrollPeriodService {
}
