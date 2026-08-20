package com.wzkris.usercenter.service;

import com.wzkris.common.orm.plus.IServicePlus;
import com.wzkris.usercenter.domain.TenantUserDO;
import jakarta.annotation.Nullable;

import java.util.List;

public interface TenantUserService extends IServicePlus<TenantUserDO> {

    /**
     * 新增信息
     *
     * @param tenantUser  信息
     * @param tenantRoleIds 关联角色
     */
    boolean saveTenantUser(TenantUserDO tenantUser, @Nullable List<Long> tenantRoleIds);

    /**
     * 修改信息
     *
     * @param tenantUser  信息
     * @param tenantRoleIds 关联角色
     */
    boolean updateTenantUser(TenantUserDO tenantUser, @Nullable List<Long> tenantRoleIds);

    /**
     * 分配角色
     *
     * @param tenantUserId 用户ID
     * @param tenantRoleIds  关联角色
     */
    boolean grantRoles(Long tenantUserId, List<Long> tenantRoleIds);

    boolean removeTenantUsers(List<Long> tenantUserIds);

    boolean existByUsername(Long tenantUserId, String username);

    boolean existByPhoneNumber(Long tenantUserId, String phoneNumber);

}
