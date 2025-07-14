package com.wzkris.user.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.wzkris.common.core.constant.CommonConstants;
import com.wzkris.common.core.constant.SecurityConstants;
import com.wzkris.common.core.exception.service.GenericException;
import com.wzkris.common.security.utils.SystemUserUtil;
import com.wzkris.user.domain.*;
import com.wzkris.user.mapper.*;
import com.wzkris.user.service.SysRoleService;
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
public class SysRoleServiceImpl implements SysRoleService {

    private final SysRoleMapper roleMapper;

    private final SysRoleMenuMapper roleMenuMapper;

    private final SysUserRoleMapper userRoleMapper;

    private final SysRoleDeptMapper roleDeptMapper;

    private final SysRoleHierarchyMapper roleHierarchyMapper;

    @Override
    public List<SysRole> listByUserId(Long userId) {
        List<Long> roleIds = userRoleMapper.listRoleIdByUserId(userId);
        if (CollectionUtils.isEmpty(roleIds)) {
            return Collections.emptyList();
        }
        // 只能查出状态正常的角色
        LambdaQueryWrapper<SysRole> lqw = new LambdaQueryWrapper<SysRole>()
                .in(SysRole::getRoleId, roleIds)
                .eq(SysRole::getStatus, CommonConstants.STATUS_ENABLE);
        return roleMapper.selectList(lqw);
    }

    @Override
    public List<Long> listIdByUserId(Long userId) {
        List<Long> roleIds = userRoleMapper.listRoleIdByUserId(userId);
        if (CollectionUtils.isEmpty(roleIds)) {
            return Collections.emptyList();
        }
        // 只能查出状态正常的角色
        LambdaQueryWrapper<SysRole> lqw = new LambdaQueryWrapper<SysRole>()
                .select(SysRole::getRoleId)
                .in(SysRole::getRoleId, roleIds)
                .eq(SysRole::getStatus, CommonConstants.STATUS_ENABLE);
        return roleMapper.selectList(lqw).stream().map(SysRole::getRoleId).toList();
    }

    @Override
    public List<SysRole> listInheritedByUserId(Long userId) {
        List<Long> roleIds = userRoleMapper.listRoleIdByUserId(userId);
        if (CollectionUtils.isEmpty(roleIds)) {
            return Collections.emptyList();
        }
        List<Long> inheritedIds = roleHierarchyMapper.listInheritedIdByRoleIds(roleIds);
        roleIds.addAll(inheritedIds);
        // 只能查出状态正常的角色
        LambdaQueryWrapper<SysRole> lqw = new LambdaQueryWrapper<SysRole>()
                .in(SysRole::getRoleId, roleIds)
                .eq(SysRole::getStatus, CommonConstants.STATUS_ENABLE);
        return roleMapper.selectList(lqw);
    }

    @Override
    public List<Long> listInheritedIdByUserId(Long userId) {
        List<Long> roleIds = userRoleMapper.listRoleIdByUserId(userId);
        if (CollectionUtils.isEmpty(roleIds)) {
            return Collections.emptyList();
        }
        List<Long> inheritedIds = roleHierarchyMapper.listInheritedIdByRoleIds(roleIds);
        roleIds.addAll(inheritedIds);
        // 只能查出状态正常的角色
        LambdaQueryWrapper<SysRole> lqw = new LambdaQueryWrapper<SysRole>()
                .select(SysRole::getRoleId)
                .in(SysRole::getRoleId, roleIds)
                .eq(SysRole::getStatus, CommonConstants.STATUS_ENABLE);
        return roleMapper.selectList(lqw).stream().map(SysRole::getRoleId).toList();
    }

    @Override
    public String getRoleGroup() {
        if (SystemUserUtil.isAdmin()) {
            return SecurityConstants.SUPER_ADMIN_NAME;
        }
        List<SysRole> roles = this.listByUserId(SystemUserUtil.getUserId());
        return roles.stream().map(SysRole::getRoleName).collect(Collectors.joining(","));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean insertRole(SysRole role, List<Long> menuIds, List<Long> deptIds, List<Long> inheritedIds) {
        // 新增角色信息
        boolean success = roleMapper.insert(role) > 0;
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
    public boolean updateRole(SysRole role, List<Long> menuIds, List<Long> deptIds, List<Long> inheritedIds) {
        // 修改角色信息
        boolean success = roleMapper.updateById(role) > 0;
        if (success && menuIds != null) {
            // 删除角色与菜单关联
            roleMenuMapper.deleteByRoleId(role.getRoleId());
            // 插入角色菜单信息
            this.insertRoleMenu(role.getRoleId(), menuIds);
        }
        if (success && deptIds != null) {
            // 删除角色与部门关联
            roleDeptMapper.deleteByRoleId(role.getRoleId());
            // 插入角色和部门信息（数据权限）
            this.insertRoleDept(role.getRoleId(), deptIds);
        }
        if (success && inheritedIds != null) {
            // 删除角色继承关系
            roleHierarchyMapper.deleteByRoleId(role.getRoleId());
            // 插入新的继承关系
            this.insertRoleHierarchy(role.getRoleId(), inheritedIds);
        }
        return success;
    }

    @Override
    public boolean allocateUsers(Long roleId, List<Long> userIds) {
        if (CollectionUtils.isNotEmpty(userIds)) {
            // 新增用户与角色管理
            List<SysUserRole> list = userIds.stream()
                    .map(userId -> new SysUserRole(userId, roleId))
                    .toList();
            return userRoleMapper.insertBatch(list) > 0;
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
            List<SysRoleMenu> list = menuIds.stream()
                    .map(menuId -> new SysRoleMenu(roleId, menuId))
                    .toList();
            roleMenuMapper.insertBatch(list);
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
            List<SysRoleDept> list = deptIds.stream()
                    .map(deptId -> new SysRoleDept(roleId, deptId))
                    .toList();
            roleDeptMapper.insertBatch(list);
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
            List<SysRoleHierarchy> list = inheritedIds.stream()
                    .map(inheritedId -> new SysRoleHierarchy(roleId, inheritedId))
                    .toList();
            roleHierarchyMapper.insertBatch(list);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteByIds(List<Long> roleIds) {
        boolean success = roleMapper.deleteByIds(roleIds) > 0;
        if (success) {
            // 删除角色与菜单关联
            roleMenuMapper.deleteByRoleIds(roleIds);
            // 删除角色与部门关联
            roleDeptMapper.deleteByRoleIds(roleIds);
            // 删除角色与用户关联
            userRoleMapper.deleteByRoleIds(roleIds);
            // 删除角色继承关系
            roleHierarchyMapper.deleteByRoleIds(roleIds);
        }
        return success;
    }

    @Override
    public void checkExistUser(List<Long> roleIds) {
        roleIds = roleIds.stream().filter(Objects::nonNull).toList();
        // 是否被用户使用
        if (userRoleMapper.existByRoleIds(roleIds)) {
            throw new GenericException("当前角色已被分配用户");
        }
    }

    @Override
    public void checkExistInherited(List<Long> roleIds) {
        roleIds = roleIds.stream().filter(Objects::nonNull).toList();
        if (roleHierarchyMapper.existByInheritedIds(roleIds)) {
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
            if (roleHierarchyMapper.existByInheritedId(roleId)) {
                throw new GenericException("当前角色已被其他角色继承");
            }
        }

        if (!roleMapper.checkInherited(roleIds, false)) {
            throw new GenericException("不允许多级继承");
        }

    }

}
