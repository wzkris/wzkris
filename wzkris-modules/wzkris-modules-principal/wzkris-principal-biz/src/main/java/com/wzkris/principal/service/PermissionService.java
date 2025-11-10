package com.wzkris.principal.service;

import com.wzkris.principal.feign.admin.resp.AdminPermissionResp;
import com.wzkris.principal.feign.member.resp.MemberPermissionResp;
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
    AdminPermissionResp getAdminPermission(Long adminId, @Nullable Long deptId);

    /**
     * 返回已授权码及数据权限
     *
     * @param memberId 成员ID
     * @param tenantId 租户ID
     * @return 权限
     */
    MemberPermissionResp getTenantPermission(Long memberId, Long tenantId);

}
