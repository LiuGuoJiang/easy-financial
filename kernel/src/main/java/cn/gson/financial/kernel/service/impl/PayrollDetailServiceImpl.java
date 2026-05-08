package cn.gson.financial.kernel.service.impl;

import cn.gson.financial.kernel.model.entity.PayrollDetail;
import cn.gson.financial.kernel.model.mapper.PayrollDetailMapper;
import cn.gson.financial.kernel.service.PayrollDetailService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

@Service
public class PayrollDetailServiceImpl extends ServiceImpl<PayrollDetailMapper, PayrollDetail> implements PayrollDetailService {
}
