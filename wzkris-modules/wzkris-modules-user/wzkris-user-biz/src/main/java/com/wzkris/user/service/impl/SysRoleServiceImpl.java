package com.wzkris.user.service.impl;

import cn.hutool.core.util.ObjUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.wzkris.common.core.constant.CommonConstants;
import com.wzkris.common.core.exception.BusinessExceptionI18n;
import com.wzkris.common.security.utils.LoginUserUtil;
import com.wzkris.user.domain.SysRole;
import com.wzkris.user.domain.SysRoleDept;
import com.wzkris.user.domain.SysRoleMenu;
import com.wzkris.user.domain.SysUserRole;
import com.wzkris.user.mapper.SysRoleDeptMapper;
import com.wzkris.user.mapper.SysRoleMapper;
import com.wzkris.user.mapper.SysRoleMenuMapper;
import com.wzkris.user.mapper.SysUserRoleMapper;
import com.wzkris.user.service.SysRoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.*;
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

    @Override
    public List<SysRole> listCanGranted() {
        return roleMapper.selectListInScope(Wrappers.lambdaQuery(SysRole.class).
                eq(SysRole::getStatus, CommonConstants.STATUS_ENABLE));
    }

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
    public String getRoleGroup() {
        if (LoginUserUtil.isAdmin()) {
            return "超级管理员";
        }
        List<SysRole> roles = this.listByUserId(LoginUserUtil.getUserId());
        return roles.stream().map(SysRole::getRoleName).collect(Collectors.joining(","));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean insertRole(SysRole role, List<Long> menuIds, List<Long> deptIds) {
        // 新增角色信息
        boolean success = roleMapper.insert(role) > 0;
        if (success) {
            // 插入角色菜单信息
            this.insertRoleMenu(role.getRoleId(), menuIds);
            // 新增角色和部门信息（数据权限）
            this.insertRoleDept(role.getRoleId(), deptIds);
        }
        return success;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateRole(SysRole role, List<Long> menuIds, List<Long> deptIds) {
        // 修改角色信息
        boolean success = roleMapper.updateById(role) > 0;
        if (success) {
            // 删除角色与菜单关联
            roleMenuMapper.deleteByRoleId(role.getRoleId());
            // 插入角色菜单信息
            this.insertRoleMenu(role.getRoleId(), menuIds);
            // 删除角色与部门关联
            roleDeptMapper.deleteByRoleId(role.getRoleId());
            // 插入角色和部门信息（数据权限）
            this.insertRoleDept(role.getRoleId(), deptIds);
        }
        return success;
    }

    @Override
    public void allocateUsers(Long roleId, List<Long> userIds) {
        if (ObjUtil.isNotEmpty(userIds)) {
            // 新增用户与角色管理
            List<SysUserRole> list = userIds.stream()
                    .map(userId -> new SysUserRole(userId, roleId))
                    .toList();
            userRoleMapper.insertBatch(list);
        }
    }

    /**
     * 新增角色菜单信息
     *
     * @param roleId  角色id
     * @param menuIds 菜单id集合
     */
    public void insertRoleMenu(Long roleId, List<Long> menuIds) {
        // 新增用户与角色管理
        if (!CollectionUtils.isEmpty(menuIds)) {
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
        if (!CollectionUtils.isEmpty(deptIds)) {
            List<SysRoleDept> list = deptIds.stream()
                    .map(deptId -> new SysRoleDept(roleId, deptId))
                    .toList();
            roleDeptMapper.insertBatch(list);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteByIds(List<Long> roleIds) {
        // 删除角色与菜单关联
        roleMenuMapper.deleteByRoleIds(roleIds);
        // 删除角色与部门关联
        roleDeptMapper.deleteByRoleIds(roleIds);
        // 删除角色与用户关联
        userRoleMapper.deleteByRoleIds(roleIds);
        roleMapper.deleteByIds(roleIds);
    }

    @Override
    public void checkRoleUse(List<Long> roleIds) {
        roleIds = roleIds.stream().filter(Objects::nonNull).toList();
        // 是否被用户使用
        if (userRoleMapper.countByRoleIds(roleIds) > 0) {
            throw new BusinessExceptionI18n("business.allocated");
        }
    }

    /**
     * 校验是否有角色的数据权限
     *
     * @param roleIds 待操作的角色id数组
     */
    public void checkDataScopes(Collection<Long> roleIds) {
        if (ObjUtil.isNotEmpty(roleIds)) {
            if (!roleMapper.checkDataScopes(new HashSet<>(roleIds))) {
                throw new AccessDeniedException("无此角色数据访问权限");
            }
        }
    }
}
