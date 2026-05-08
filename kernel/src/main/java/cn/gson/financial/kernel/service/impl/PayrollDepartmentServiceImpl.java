package cn.gson.financial.kernel.service.impl;

import cn.gson.financial.kernel.model.entity.PayrollDepartment;
import cn.gson.financial.kernel.model.mapper.PayrollDepartmentMapper;
import cn.gson.financial.kernel.service.PayrollDepartmentService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

@Service
public class PayrollDepartmentServiceImpl extends ServiceImpl<PayrollDepartmentMapper, PayrollDepartment> implements PayrollDepartmentService {
}
