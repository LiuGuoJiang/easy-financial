package cn.gson.financial.kernel.service.impl;

import cn.gson.financial.kernel.model.entity.PayrollItem;
import cn.gson.financial.kernel.model.mapper.PayrollItemMapper;
import cn.gson.financial.kernel.service.PayrollItemService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

@Service
public class PayrollItemServiceImpl extends ServiceImpl<PayrollItemMapper, PayrollItem> implements PayrollItemService {
}
