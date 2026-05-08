package cn.gson.financial.kernel.service;

import cn.gson.financial.kernel.model.entity.PaymentBill;
import cn.gson.financial.kernel.model.vo.UserVo;
import com.baomidou.mybatisplus.extension.service.IService;

public interface PaymentBillService extends IService<PaymentBill> {
    void audit(Integer accountSetsId, Integer id, UserVo user, Boolean generateVoucher);

    void cancelAudit(Integer accountSetsId, Integer id);
}
