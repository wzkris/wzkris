package com.wzkris.user.service;

import com.wzkris.user.rmi.domain.resp.SysPermissionResp;
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
    SysPermissionResp getPermission(Long userId, Long tenantId, @Nullable Long deptId);

}
