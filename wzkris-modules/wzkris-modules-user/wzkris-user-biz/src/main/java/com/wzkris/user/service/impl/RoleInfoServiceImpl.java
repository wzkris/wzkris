package com.wzkris.user.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.wzkris.common.core.constant.CommonConstants;
import com.wzkris.common.core.constant.SecurityConstants;
import com.wzkris.common.core.exception.service.GenericException;
import com.wzkris.common.security.utils.LoginUserUtil;
import com.wzkris.user.domain.*;
import com.wzkris.user.mapper.*;
import com.wzkris.user.service.RoleInfoService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 角色 业务层处理
 *
 * @author wzkris
 */
@Service
@RequiredArgsConstructor
public class RoleInfoServiceImpl implements RoleInfoService {

    private final RoleInfoMapper roleInfoMapper;

    private final RoleToMenuMapper roleToMenuMapper;

    private final UserToRoleMapper userToRoleMapper;

    private final RoleToDeptMapper roleToDeptMapper;

    private final RoleToHierarchyMapper roleToHierarchyMapper;

    @Override
    public List<RoleInfoDO> listByUserId(Long userId) {
        List<Long> roleIds = userToRoleMapper.listRoleIdByUserId(userId);
        if (CollectionUtils.isEmpty(roleIds)) {
            return Collections.emptyList();
        }
        // 只能查出状态正常的角色
        LambdaQueryWrapper<RoleInfoDO> lqw = new LambdaQueryWrapper<RoleInfoDO>()
                .in(RoleInfoDO::getRoleId, roleIds)
                .eq(RoleInfoDO::getStatus, CommonConstants.STATUS_ENABLE);
        return roleInfoMapper.selectList(lqw);
    }

    @Override
    public List<Long> listIdByUserId(Long userId) {
        List<Long> roleIds = userToRoleMapper.listRoleIdByUserId(userId);
        if (CollectionUtils.isEmpty(roleIds)) {
            return Collections.emptyList();
        }
        // 只能查出状态正常的角色
        LambdaQueryWrapper<RoleInfoDO> lqw = new LambdaQueryWrapper<RoleInfoDO>()
                .select(RoleInfoDO::getRoleId)
                .in(RoleInfoDO::getRoleId, roleIds)
                .eq(RoleInfoDO::getStatus, CommonConstants.STATUS_ENABLE);
        return roleInfoMapper.selectList(lqw).stream().map(RoleInfoDO::getRoleId).toList();
    }

    @Override
    public List<RoleInfoDO> listInheritedByUserId(Long userId) {
        List<Long> roleIds = userToRoleMapper.listRoleIdByUserId(userId);
        if (CollectionUtils.isEmpty(roleIds)) {
            return Collections.emptyList();
        }
        List<Long> inheritedIds = roleToHierarchyMapper.listInheritedIdByRoleIds(roleIds);
        roleIds.addAll(inheritedIds);
        // 只能查出状态正常的角色
        LambdaQueryWrapper<RoleInfoDO> lqw = new LambdaQueryWrapper<RoleInfoDO>()
                .in(RoleInfoDO::getRoleId, roleIds)
                .eq(RoleInfoDO::getStatus, CommonConstants.STATUS_ENABLE);
        return roleInfoMapper.selectList(lqw);
    }

    @Override
    public List<Long> listInheritedIdByUserId(Long userId) {
        List<Long> roleIds = userToRoleMapper.listRoleIdByUserId(userId);
        if (CollectionUtils.isEmpty(roleIds)) {
            return Collections.emptyList();
        }
        List<Long> inheritedIds = roleToHierarchyMapper.listInheritedIdByRoleIds(roleIds);
        roleIds.addAll(inheritedIds);
        // 只能查出状态正常的角色
        LambdaQueryWrapper<RoleInfoDO> lqw = new LambdaQueryWrapper<RoleInfoDO>()
                .select(RoleInfoDO::getRoleId)
                .in(RoleInfoDO::getRoleId, roleIds)
                .eq(RoleInfoDO::getStatus, CommonConstants.STATUS_ENABLE);
        return roleInfoMapper.selectList(lqw).stream().map(RoleInfoDO::getRoleId).toList();
    }

    @Override
    public String getRoleGroup() {
        if (LoginUserUtil.isAdmin()) {
            return SecurityConstants.SUPER_ADMIN_NAME;
        }
        List<RoleInfoDO> roles = this.listByUserId(LoginUserUtil.getId());
        return roles.stream().map(RoleInfoDO::getRoleName).collect(Collectors.joining(","));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean saveRole(RoleInfoDO role, List<Long> menuIds, List<Long> deptIds, List<Long> inheritedIds) {
        // 新增角色信息
        boolean success = roleInfoMapper.insert(role) > 0;
        if (success) {
            // 新增角色菜单信息
            this.insertRoleMenu(role.getRoleId(), menuIds);
            // 新增角色和部门信息（数据权限）
            this.insertRoleDept(role.getRoleId(), deptIds);
            // 新增角色继承关系
            this.insertRoleHierarchy(role.getRoleId(), inheritedIds);
        }
        return success;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean modifyRole(RoleInfoDO role, List<Long> menuIds, List<Long> deptIds, List<Long> inheritedIds) {
        // 修改角色信息
        boolean success = roleInfoMapper.updateById(role) > 0;
        if (success && menuIds != null) {
            // 删除角色与菜单关联
            roleToMenuMapper.deleteByRoleId(role.getRoleId());
            // 插入角色菜单信息
            this.insertRoleMenu(role.getRoleId(), menuIds);
        }
        if (success && deptIds != null) {
            // 删除角色与部门关联
            roleToDeptMapper.deleteByRoleId(role.getRoleId());
            // 插入角色和部门信息（数据权限）
            this.insertRoleDept(role.getRoleId(), deptIds);
        }
        if (success && inheritedIds != null) {
            // 删除角色继承关系
            roleToHierarchyMapper.deleteByRoleId(role.getRoleId());
            // 插入新的继承关系
            this.insertRoleHierarchy(role.getRoleId(), inheritedIds);
        }
        return success;
    }

    @Override
    public boolean grantUsers(Long roleId, List<Long> userIds) {
        if (CollectionUtils.isNotEmpty(userIds)) {
            // 新增用户与角色管理
            List<UserToRoleDO> list = userIds.stream()
                    .map(userId -> new UserToRoleDO(userId, roleId))
                    .toList();
            return userToRoleMapper.insertBatch(list) > 0;
        }
        return false;
    }

    @Override
    public boolean ungrantUsers(Long roleId, List<Long> userIds) {
        if (CollectionUtils.isNotEmpty(userIds)) {
            return userToRoleMapper.deleteBatch(roleId, userIds) > 0;
        }
        return false;
    }

    /**
     * 新增角色菜单信息
     *
     * @param roleId  角色id
     * @param menuIds 菜单id集合
     */
    public void insertRoleMenu(Long roleId, List<Long> menuIds) {
        if (CollectionUtils.isNotEmpty(menuIds)) {
            List<RoleToMenuDO> list = menuIds.stream()
                    .map(menuId -> new RoleToMenuDO(roleId, menuId))
                    .toList();
            roleToMenuMapper.insertBatch(list);
        }
    }

    /**
     * 新增角色部门信息(数据权限)
     *
     * @param roleId  角色id
     * @param deptIds 部门id集合
     */
    public void insertRoleDept(Long roleId, List<Long> deptIds) {
        if (CollectionUtils.isNotEmpty(deptIds)) {
            List<RoleToDepartmentDO> list = deptIds.stream()
                    .map(deptId -> new RoleToDepartmentDO(roleId, deptId))
                    .toList();
            roleToDeptMapper.insertBatch(list);
        }
    }

    /**
     * 新增角色继承信息
     *
     * @param roleId       角色id
     * @param inheritedIds 继承角色id集合
     */
    private void insertRoleHierarchy(Long roleId, List<Long> inheritedIds) {
        if (CollectionUtils.isNotEmpty(inheritedIds)) {
            List<RoleToHierarchyDO> list = inheritedIds.stream()
                    .map(inheritedId -> new RoleToHierarchyDO(roleId, inheritedId))
                    .toList();
            roleToHierarchyMapper.insertBatch(list);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean removeByIds(List<Long> roleIds) {
        boolean success = roleInfoMapper.deleteByIds(roleIds) > 0;
        if (success) {
            // 删除角色与菜单关联
            roleToMenuMapper.deleteByRoleIds(roleIds);
            // 删除角色与部门关联
            roleToDeptMapper.deleteByRoleIds(roleIds);
            // 删除角色与用户关联
            userToRoleMapper.deleteByRoleIds(roleIds);
            // 删除角色继承关系
            roleToHierarchyMapper.deleteByRoleIds(roleIds);
        }
        return success;
    }

    @Override
    public void existUser(List<Long> roleIds) {
        roleIds = roleIds.stream().filter(Objects::nonNull).toList();
        // 是否被用户使用
        if (userToRoleMapper.existByRoleIds(roleIds)) {
            throw new GenericException("当前角色已被分配用户");
        }
    }

    @Override
    public void existInherited(List<Long> roleIds) {
        roleIds = roleIds.stream().filter(Objects::nonNull).toList();
        if (roleToHierarchyMapper.existByInheritedIds(roleIds)) {
            throw new GenericException("当前角色已被其他角色继承");
        }
    }

    @Override
    public void checkInheritedRole(Long roleId, List<Long> roleIds) {
        if (roleIds == null || roleIds.isEmpty()) {
            return;
        }

        if (roleId != null) {
            if (roleIds.contains(roleId)) {
                throw new GenericException("不能继承自己");
            }
            if (roleToHierarchyMapper.existByInheritedId(roleId)) {
                throw new GenericException("当前角色已被其他角色继承");
            }
        }

        if (!roleInfoMapper.checkInherited(roleIds, false)) {
            throw new GenericException("不允许多级继承");
        }

    }

}
