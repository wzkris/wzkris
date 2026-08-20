package com.wzkris.usercenter.service;

import com.wzkris.common.orm.plus.IServicePlus;
import com.wzkris.usercenter.domain.RoleInfoDO;
import com.wzkris.usercenter.response.SelectResponse;
import jakarta.annotation.Nullable;

import java.util.List;

public interface RoleInfoService extends IServicePlus<RoleInfoDO> {

    /**
     * 根据管理员ID查询关联角色(正常状态)
     *
     * @param adminId          管理员ID
     * @param includeInherited 是否包含继承角色
     * @return 角色列表
     */
    List<RoleInfoDO> listByAdminId(Long adminId, boolean includeInherited);

    boolean saveRole(RoleInfoDO role, @Nullable List<Long> menuIds, @Nullable List<Long> deptIds, @Nullable List<Long> childIds);

    boolean updateRole(RoleInfoDO role, @Nullable List<Long> menuIds, @Nullable List<Long> deptIds, @Nullable List<Long> childIds);

    boolean removeRoles(List<Long> roleIds);

    boolean existAdmin(List<Long> roleIds);

    boolean existChildRole(List<Long> roleIds);

    List<SelectResponse> listRoleSelect(String roleName);

}
