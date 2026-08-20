package com.wzkris.usercenter.service;

import com.wzkris.common.orm.plus.IServicePlus;
import com.wzkris.usercenter.domain.TenantRoleDO;
import com.wzkris.usercenter.response.SelectResponse;
import jakarta.annotation.Nullable;

import java.util.List;

public interface TenantRoleService extends IServicePlus<TenantRoleDO> {

    List<TenantRoleDO> listByTenantUserId(Long tenantUserId);

    List<SelectResponse> listSelect(@Nullable String roleName);

    boolean saveRole(TenantRoleDO role, List<Long> menuIds);

    boolean updateRole(TenantRoleDO role, List<Long> menuIds);

    boolean removeRoles(List<Long> tenantRoleIds);

    boolean existTenantUser(List<Long> tenantRoleIds);

}
