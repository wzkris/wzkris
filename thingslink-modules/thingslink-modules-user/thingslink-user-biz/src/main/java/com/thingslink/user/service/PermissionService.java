package com.thingslink.user.service;

import com.thingslink.user.api.domain.dto.SysPermissionDTO;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;

/**
 * 权限信息 服务层
 *
 * @author wzkris
 */
public interface PermissionService {

    /**
     * 返回已授权码及数据权限
     *
     * @param userId   用户ID
     * @param tenantId 租户ID
     * @param deptId   部门ID
     * @return 权限
     */
    SysPermissionDTO getPermission(@Nonnull Long userId, @Nonnull Long tenantId, @Nullable Long deptId);

}
