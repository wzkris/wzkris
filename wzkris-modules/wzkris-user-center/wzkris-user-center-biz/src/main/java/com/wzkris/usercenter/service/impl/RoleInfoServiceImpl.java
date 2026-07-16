package com.wzkris.usercenter.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.wzkris.common.orm.plus.ServiceImplPlus;
import com.wzkris.common.core.utils.StringUtil;
import com.wzkris.usercenter.domain.RoleInfoDO;
import com.wzkris.usercenter.domain.RoleInheritanceDO;
import com.wzkris.usercenter.domain.RoleToDeptDO;
import com.wzkris.usercenter.domain.RoleToMenuDO;
import com.wzkris.usercenter.enums.role.RoleStatusEnum;
import com.wzkris.usercenter.mapper.*;
import com.wzkris.usercenter.response.SelectResponse;
import com.wzkris.usercenter.service.RoleInfoService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class RoleInfoServiceImpl
        extends ServiceImplPlus<RoleInfoMapper, RoleInfoDO>
        implements RoleInfoService {

    private final RoleToMenuMapper roleToMenuMapper;

    private final AdminToRoleMapper adminToRoleMapper;

    private final RoleToDeptMapper roleToDeptMapper;

    private final RoleInheritanceMapper roleInheritanceMapper;

    @Override
    public List<RoleInfoDO> listByAdminId(Long adminId, boolean includeInherited) {
        List<Long> roleIds = listRoleIdsByAdminId(adminId, includeInherited);
        if (CollectionUtils.isEmpty(roleIds)) {
            return Collections.emptyList();
        }
        return this.lambdaQuery()
                .in(RoleInfoDO::getRoleId, roleIds)
                .eq(RoleInfoDO::getStatus, RoleStatusEnum.ENABLE)
                .list();
    }

    private List<Long> listRoleIdsByAdminId(Long adminId, boolean includeInherited) {
        List<Long> roleIds = adminToRoleMapper.listRoleIdByAdminId(adminId);
        if (CollectionUtils.isEmpty(roleIds)) {
            return Collections.emptyList();
        }
        if (includeInherited) {
            List<Long> childRoleIds = roleInheritanceMapper.listChildIdsRecursive(roleIds);
            if (CollectionUtils.isNotEmpty(childRoleIds)) {
                roleIds.addAll(childRoleIds);
            }
        }
        return roleIds;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean saveRole(RoleInfoDO role, List<Long> menuIds, List<Long> deptIds, List<Long> childIds) {
        boolean success = baseMapper.insert(role) > 0;
        if (success) {
            this.insertRoleMenu(role.getRoleId(), menuIds);
            this.insertRoleDept(role.getRoleId(), deptIds);
            this.insertRoleInheritance(role.getRoleId(), childIds);
        }
        return success;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateRole(RoleInfoDO role, List<Long> menuIds, List<Long> deptIds, List<Long> childIds) {
        boolean success = baseMapper.updateById(role) > 0;
        if (success) {
            if (childIds != null) {
                roleInheritanceMapper.deleteByRoleId(role.getRoleId());
                this.insertRoleInheritance(role.getRoleId(), childIds);
            }
            if (menuIds != null) {
                roleToMenuMapper.deleteByRoleId(role.getRoleId());
                this.insertRoleMenu(role.getRoleId(), menuIds);
            }
            if (deptIds != null) {
                roleToDeptMapper.deleteByRoleId(role.getRoleId());
                this.insertRoleDept(role.getRoleId(), deptIds);
            }
        }
        return success;
    }

    public void insertRoleMenu(Long roleId, List<Long> menuIds) {
        if (CollectionUtils.isNotEmpty(menuIds)) {
            List<RoleToMenuDO> list = menuIds.stream()
                    .map(menuId -> new RoleToMenuDO(roleId, menuId))
                    .toList();
            roleToMenuMapper.insert(list);
        }
    }

    public void insertRoleDept(Long roleId, List<Long> deptIds) {
        if (CollectionUtils.isNotEmpty(deptIds)) {
            List<RoleToDeptDO> list = deptIds.stream()
                    .map(deptId -> new RoleToDeptDO(roleId, deptId))
                    .toList();
            roleToDeptMapper.insert(list);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean removeRoles(List<Long> roleIds) {
        boolean success = baseMapper.deleteByIds(roleIds) > 0;
        if (success) {
            roleToMenuMapper.deleteByRoleIds(roleIds);
            roleToDeptMapper.deleteByRoleIds(roleIds);
            adminToRoleMapper.deleteByRoleIds(roleIds);
            roleInheritanceMapper.deleteByRoleIds(roleIds);
            roleInheritanceMapper.deleteByChildIds(roleIds);
        }
        return success;
    }

    @Override
    public boolean existAdmin(List<Long> roleIds) {
        roleIds = roleIds.stream().filter(Objects::nonNull).toList();
        return adminToRoleMapper.existByRoleIds(roleIds);
    }

    @Override
    public boolean existChildRole(List<Long> roleIds) {
        if (CollectionUtils.isEmpty(roleIds)) {
            return false;
        }
        return roleInheritanceMapper.existChildRole(roleIds) > 0;
    }

    private void insertRoleInheritance(Long roleId, List<Long> childIds) {
        if (CollectionUtils.isNotEmpty(childIds)) {
            List<RoleInheritanceDO> list = childIds.stream()
                    .map(childId -> new RoleInheritanceDO(roleId, childId))
                    .toList();
            roleInheritanceMapper.insert(list);
        }
    }

    @Override
    public List<SelectResponse> listRoleSelect(String roleName) {
        return this.lambdaQuery()
                .like(StringUtil.isNotBlank(roleName), RoleInfoDO::getRoleName, roleName)
                .list()
                .stream()
                .map(roleInfoDO -> {
                    SelectResponse selectResponse = new SelectResponse();
                    selectResponse.setId(roleInfoDO.getRoleId());
                    selectResponse.setLabel(roleInfoDO.getRoleName());
                    return selectResponse;
                })
                .toList();
    }

}
