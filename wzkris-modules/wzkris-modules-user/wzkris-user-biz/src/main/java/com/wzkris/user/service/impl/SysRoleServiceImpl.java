package com.wzkris.user.service.impl;

import cn.hutool.core.util.ObjUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.wzkris.common.core.constant.CommonConstants;
import com.wzkris.common.core.constant.SecurityConstants;
import com.wzkris.common.core.exception.service.BusinessException;
import com.wzkris.common.core.utils.StringUtil;
import com.wzkris.common.security.utils.LoginUtil;
import com.wzkris.user.domain.SysRole;
import com.wzkris.user.domain.SysRoleDept;
import com.wzkris.user.domain.SysRoleMenu;
import com.wzkris.user.domain.SysUserRole;
import com.wzkris.user.domain.vo.SelectVO;
import com.wzkris.user.mapper.SysRoleDeptMapper;
import com.wzkris.user.mapper.SysRoleMapper;
import com.wzkris.user.mapper.SysRoleMenuMapper;
import com.wzkris.user.mapper.SysUserRoleMapper;
import com.wzkris.user.service.SysRoleService;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

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
    public List<SelectVO> listSelect(String roleName) {
        return roleMapper
                .selectLists(Wrappers.lambdaQuery(SysRole.class)
                        .select(SysRole::getRoleId, SysRole::getRoleName)
                        .eq(SysRole::getStatus, CommonConstants.STATUS_ENABLE)
                        .like(StringUtil.isNotBlank(roleName), SysRole::getRoleName, roleName)
                        .orderByAsc(SysRole::getRoleId))
                .stream()
                .map(SelectVO::new)
                .collect(Collectors.toList());
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
        if (LoginUtil.isAdmin()) {
            return SecurityConstants.SUPER_ADMIN_NAME;
        }
        List<SysRole> roles = this.listByUserId(LoginUtil.getUserId());
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
    public boolean allocateUsers(Long roleId, List<Long> userIds) {
        if (ObjUtil.isNotEmpty(userIds)) {
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
    public boolean deleteByIds(List<Long> roleIds) {
        boolean success = roleMapper.deleteByIds(roleIds) > 0;
        if (success) {
            // 删除角色与菜单关联
            roleMenuMapper.deleteByRoleIds(roleIds);
            // 删除角色与部门关联
            roleDeptMapper.deleteByRoleIds(roleIds);
            // 删除角色与用户关联
            userRoleMapper.deleteByRoleIds(roleIds);
        }
        return success;
    }

    @Override
    public void checkRoleUsed(List<Long> roleIds) {
        roleIds = roleIds.stream().filter(Objects::nonNull).toList();
        // 是否被用户使用
        if (userRoleMapper.checkExistByRoleIds(roleIds)) {
            throw new BusinessException("business.allocated");
        }
    }

    /**
     * 校验是否有角色的数据权限
     *
     * @param roleIds 待操作的角色id数组
     */
    public void checkDataScopes(Collection<Long> roleIds) {
        if (ObjUtil.isNotEmpty(roleIds)) {
            if (!roleMapper.checkDataScopes(roleIds)) {
                throw new AccessDeniedException("无此角色数据访问权限");
            }
        }
    }
}
