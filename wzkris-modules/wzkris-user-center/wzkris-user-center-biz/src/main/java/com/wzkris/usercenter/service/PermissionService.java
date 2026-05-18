package com.wzkris.usercenter.service;

import com.wzkris.usercenter.remote.api.admin.response.AdminPermissionResponse;
import com.wzkris.usercenter.remote.api.member.response.MemberPermissionResponse;
import jakarta.annotation.Nullable;

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
    AdminPermissionResponse getAdminPermission(Long adminId, @Nullable Long deptId);

    /**
     * 返回已授权码及数据权限
     *
     * @param memberId 成员ID
     * @param tenantId 租户ID
     * @return 权限
     */
    MemberPermissionResponse getTenantPermission(Long memberId, Long tenantId);

}

