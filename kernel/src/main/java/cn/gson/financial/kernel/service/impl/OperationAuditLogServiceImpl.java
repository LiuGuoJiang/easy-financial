package cn.gson.financial.kernel.service.impl;

import cn.gson.financial.kernel.model.entity.OperationAuditLog;
import cn.gson.financial.kernel.model.mapper.OperationAuditLogMapper;
import cn.gson.financial.kernel.service.OperationAuditLogService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

@Service
public class OperationAuditLogServiceImpl extends ServiceImpl<OperationAuditLogMapper, OperationAuditLog> implements OperationAuditLogService {
}
