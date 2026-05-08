package cn.gson.financial.kernel.service.impl;

import cn.gson.financial.kernel.model.entity.PayrollSheet;
import cn.gson.financial.kernel.model.mapper.PayrollSheetMapper;
import cn.gson.financial.kernel.service.PayrollSheetService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

@Service
public class PayrollSheetServiceImpl extends ServiceImpl<PayrollSheetMapper, PayrollSheet> implements PayrollSheetService {
}
