package com.thingslink.user.service;

import com.thingslink.user.api.domain.dto.SysPermissionDTO;
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
     * @param userId 用户ID
     * @param deptId 部门ID
     * @return 权限
     */
    SysPermissionDTO getPermission(@Nonnull Long userId, @Nullable Long deptId);

}
