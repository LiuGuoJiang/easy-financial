package cn.gson.financial.kernel.service.impl;

import cn.gson.financial.kernel.model.entity.MerchantOrder;
import cn.gson.financial.kernel.model.mapper.MerchantOrderMapper;
import cn.gson.financial.kernel.service.MerchantOrderService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

@Service
public class MerchantOrderServiceImpl extends ServiceImpl<MerchantOrderMapper, MerchantOrder> implements MerchantOrderService {
}
