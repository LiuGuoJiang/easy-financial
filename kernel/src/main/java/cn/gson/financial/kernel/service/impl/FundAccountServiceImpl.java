package cn.gson.financial.kernel.service.impl;

import cn.gson.financial.kernel.model.entity.FundAccount;
import cn.gson.financial.kernel.model.mapper.FundAccountMapper;
import cn.gson.financial.kernel.service.FundAccountService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class FundAccountServiceImpl extends ServiceImpl<FundAccountMapper, FundAccount> implements FundAccountService {
    @Override
    public boolean save(FundAccount entity) {
        if (entity.getStatus() == null) {
            entity.setStatus(true);
        }
        if (entity.getOpeningBalance() == null) {
            entity.setOpeningBalance(0d);
        }
        if (entity.getCurrentBalance() == null) {
            entity.setCurrentBalance(entity.getOpeningBalance());
        }
        if (entity.getCreateDate() == null) {
            entity.setCreateDate(new Date());
        }
        return super.save(entity);
    }
}
