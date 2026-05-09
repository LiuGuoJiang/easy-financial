package cn.gson.financial.controller;

import cn.gson.financial.base.BaseCrudController;
import cn.gson.financial.kernel.model.entity.OperationAuditLog;
import cn.gson.financial.kernel.service.OperationAuditLogService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 商业化运营后台 - AUDIT_LOG 管理。
 */
@RestController
@RequestMapping("/admin/audit-logs")
public class OperationAuditLogController extends BaseCrudController<OperationAuditLogService, OperationAuditLog> {
}
