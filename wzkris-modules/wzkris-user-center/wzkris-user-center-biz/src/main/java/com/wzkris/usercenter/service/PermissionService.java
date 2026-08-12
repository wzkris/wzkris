package com.wzkris.usercenter.service;

import com.wzkris.common.core.model.UserRole;
import jakarta.annotation.Nullable;

import java.util.List;

/**
 * 系统权限信息 服务层
 *
 * @author wzkris
 */
public interface PermissionService {

    /**
     * 返回已授权码及数据权限
     *
     * @param adminId 管理员ID
     * @param deptId  部门ID
     * @return 权限
     */
    List<UserRole> getAdminPermission(Long adminId, @Nullable Long deptId);

    /**
     * 返回已授权码及数据权限
     *
     * @param tenantUserId 用户ID
     * @param tenantId 租户ID
     * @return 权限
     */
    List<UserRole> getTenantPermission(Long tenantUserId, Long tenantId);

}

