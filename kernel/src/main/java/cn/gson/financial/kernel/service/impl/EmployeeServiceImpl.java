package cn.gson.financial.kernel.service.impl;

import cn.gson.financial.kernel.model.entity.Employee;
import cn.gson.financial.kernel.model.mapper.EmployeeMapper;
import cn.gson.financial.kernel.service.EmployeeService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

@Service
public class EmployeeServiceImpl extends ServiceImpl<EmployeeMapper, Employee> implements EmployeeService {
}
