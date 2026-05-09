package cn.gson.financial.kernel.service.impl;

import cn.gson.financial.kernel.model.entity.Subscription;
import cn.gson.financial.kernel.model.mapper.SubscriptionMapper;
import cn.gson.financial.kernel.service.SubscriptionService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

@Service
public class SubscriptionServiceImpl extends ServiceImpl<SubscriptionMapper, Subscription> implements SubscriptionService {
}
