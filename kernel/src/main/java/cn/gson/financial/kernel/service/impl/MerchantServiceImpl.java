package cn.gson.financial.kernel.service.impl;

import cn.gson.financial.kernel.model.entity.Merchant;
import cn.gson.financial.kernel.model.mapper.MerchantMapper;
import cn.gson.financial.kernel.service.MerchantService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

@Service
public class MerchantServiceImpl extends ServiceImpl<MerchantMapper, Merchant> implements MerchantService {
}
