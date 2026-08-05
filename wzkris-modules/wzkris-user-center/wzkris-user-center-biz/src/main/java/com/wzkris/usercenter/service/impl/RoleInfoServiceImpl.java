package com.wzkris.usercenter.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.wzkris.common.core.utils.StringUtil;
import com.wzkris.common.orm.plus.ServiceImplPlus;
import com.wzkris.usercenter.domain.AdminToRoleDO;
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

import java.util.ArrayList;
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
                .in(RoleInfoDO::getId, roleIds)
                .eq(RoleInfoDO::getStatus, RoleStatusEnum.ENABLE)
                .list();
    }

    private List<Long> listRoleIdsByAdminId(Long adminId, boolean includeInherited) {
        List<Long> roleIds = new ArrayList<>(adminToRoleMapper.selectObjsByObj(
                AdminToRoleDO::getRoleId, AdminToRoleDO::getAdminId, adminId));
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
            this.insertRoleMenu(role.getId(), menuIds);
            this.insertRoleDept(role.getId(), deptIds);
            this.insertRoleInheritance(role.getId(), childIds);
        }
        return success;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateRole(RoleInfoDO role, List<Long> menuIds, List<Long> deptIds, List<Long> childIds) {
        boolean success = baseMapper.updateById(role) > 0;
        if (success) {
            if (childIds != null) {
                roleInheritanceMapper.delete(new LambdaQueryWrapper<>(RoleInheritanceDO.class)
                        .eq(RoleInheritanceDO::getRoleId, role.getId()));
                this.insertRoleInheritance(role.getId(), childIds);
            }
            if (menuIds != null) {
                roleToMenuMapper.delete(new LambdaQueryWrapper<>(RoleToMenuDO.class)
                        .eq(RoleToMenuDO::getRoleId, role.getId()));
                this.insertRoleMenu(role.getId(), menuIds);
            }
            if (deptIds != null) {
                roleToDeptMapper.delete(new LambdaQueryWrapper<>(RoleToDeptDO.class)
                        .eq(RoleToDeptDO::getRoleId, role.getId()));
                this.insertRoleDept(role.getId(), deptIds);
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
            roleToMenuMapper.delete(new LambdaQueryWrapper<>(RoleToMenuDO.class)
                    .in(RoleToMenuDO::getRoleId, roleIds));
            roleToDeptMapper.delete(new LambdaQueryWrapper<>(RoleToDeptDO.class)
                    .in(RoleToDeptDO::getRoleId, roleIds));
            adminToRoleMapper.delete(new LambdaQueryWrapper<>(AdminToRoleDO.class)
                    .in(AdminToRoleDO::getRoleId, roleIds));
            roleInheritanceMapper.delete(new LambdaQueryWrapper<>(RoleInheritanceDO.class)
                    .in(RoleInheritanceDO::getRoleId, roleIds));
            roleInheritanceMapper.delete(new LambdaQueryWrapper<>(RoleInheritanceDO.class)
                    .in(RoleInheritanceDO::getChildId, roleIds));
        }
        return success;
    }

    @Override
    public boolean existAdmin(List<Long> roleIds) {
        roleIds = roleIds.stream().filter(Objects::nonNull).toList();
        if (CollectionUtils.isEmpty(roleIds)) {
            return false;
        }
        return adminToRoleMapper.exists(new LambdaQueryWrapper<>(AdminToRoleDO.class)
                .in(AdminToRoleDO::getRoleId, roleIds));
    }

    @Override
    public boolean existChildRole(List<Long> roleIds) {
        if (CollectionUtils.isEmpty(roleIds)) {
            return false;
        }
        return roleInheritanceMapper.exists(new LambdaQueryWrapper<>(RoleInheritanceDO.class)
                .in(RoleInheritanceDO::getChildId, roleIds));
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
                    selectResponse.setId(roleInfoDO.getId());
                    selectResponse.setLabel(roleInfoDO.getRoleName());
                    return selectResponse;
                })
                .toList();
    }

}
