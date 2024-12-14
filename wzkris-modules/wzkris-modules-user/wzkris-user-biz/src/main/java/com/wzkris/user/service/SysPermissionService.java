package com.wzkris.user.service;

import com.wzkris.user.api.domain.response.SysPermissionResp;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;

/**
 * 系统权限信息 服务层
 *
 * @author wzkris
 */
public interface SysPermissionService {

    /**
     * 返回已授权码及数据权限
     *
     * @param userId   用户ID
     * @param tenantId 租户ID
     * @param deptId   部门ID
     * @return 权限
     */
    SysPermissionResp getPermission(@Nonnull Long userId, @Nonnull Long tenantId, @Nullable Long deptId);

}
