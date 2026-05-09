package cn.gson.financial.base;

import cn.gson.financial.kernel.controller.JsonResult;
import cn.gson.financial.kernel.model.entity.OperationAuditLog;
import cn.gson.financial.kernel.service.OperationAuditLogService;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.beans.factory.annotation.Autowired;

import java.lang.reflect.Field;
import java.util.Date;

/**
 * 商业化运营后台 CRUD 基类，统一记录平台管理员变更审计日志。
 */
public abstract class AdminCrudController<T extends IService, E> extends BaseCrudController<T, E> {

    @Autowired
    private OperationAuditLogService operationAuditLogService;

    protected abstract String targetType();

    @Override
    public JsonResult save(E entity) {
        JsonResult result = super.save(entity);
        if (result.isSuccess()) {
            audit("CREATE", null, entity);
        }
        return result;
    }

    @Override
    public JsonResult update(E entity) {
        Object before = loadTarget(entity);
        JsonResult result = super.update(entity);
        if (result.isSuccess()) {
            audit("UPDATE", before, entity);
        }
        return result;
    }

    @Override
    public JsonResult delete(Long id) {
        Object before = service.getById(id);
        JsonResult result = super.delete(id);
        if (result.isSuccess()) {
            audit("DELETE", before, null);
        }
        return result;
    }

    private Object loadTarget(E entity) {
        try {
            Field field = entity.getClass().getDeclaredField("id");
            field.setAccessible(true);
            Object id = field.get(entity);
            return id == null ? null : service.getById((Integer) id);
        } catch (Exception ex) {
            return null;
        }
    }

    private void audit(String action, Object before, Object after) {
        OperationAuditLog log = new OperationAuditLog();
        log.setTenantId(tenantId);
        if (currentUser != null) {
            log.setOperatorId(currentUser.getId());
            log.setOperatorName(currentUser.getRealName() == null ? currentUser.getMobile() : currentUser.getRealName());
        }
        log.setTargetType(targetType());
        log.setTargetId(resolveTargetId(after == null ? before : after));
        log.setAction(action);
        log.setBeforeValue(before == null ? null : JSON.toJSONString(before));
        log.setAfterValue(after == null ? null : JSON.toJSONString(after));
        log.setCreateDate(new Date());
        if (request != null) {
            log.setIp(request.getRemoteAddr());
        }
        operationAuditLogService.save(log);
    }

    private Integer resolveTargetId(Object entity) {
        if (entity == null) {
            return null;
        }
        try {
            Field field = entity.getClass().getDeclaredField("id");
            field.setAccessible(true);
            Object value = field.get(entity);
            return value instanceof Integer ? (Integer) value : null;
        } catch (Exception ex) {
            return null;
        }
    }
}
