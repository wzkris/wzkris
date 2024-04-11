package com.thingslink.user.service.impl;

import cn.hutool.core.util.ObjUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.thingslink.user.domain.SysRole;
import com.thingslink.user.domain.SysRoleDept;
import com.thingslink.user.domain.SysRoleMenu;
import com.thingslink.user.domain.SysUserRole;
import com.thingslink.user.domain.dto.SysRoleDTO;
import com.thingslink.user.mapper.SysRoleDeptMapper;
import com.thingslink.user.mapper.SysRoleMapper;
import com.thingslink.user.mapper.SysRoleMenuMapper;
import com.thingslink.user.mapper.SysUserRoleMapper;
import com.thingslink.user.service.SysRoleService;
import com.thingslink.common.core.constant.CommonConstants;
import com.thingslink.common.core.exception.BusinessExceptionI18n;
import com.thingslink.common.core.utils.StringUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.Arrays;
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
    private final SysRoleMapper sysRoleMapper;
    private final SysRoleMenuMapper sysRoleMenuMapper;
    private final SysUserRoleMapper sysUserRoleMapper;
    private final SysRoleDeptMapper sysRoleDeptMapper;

    /**
     * 根据条件查询角色列表
     *
     * @param role 条件
     * @return 角色列表
     */
    @Override
    public List<SysRole> list(SysRole role) {
        LambdaQueryWrapper<SysRole> lqw = this.buildQueryWrapper(role);
        return sysRoleMapper.selectListInScope(lqw);
    }

    private LambdaQueryWrapper<SysRole> buildQueryWrapper(SysRole sysRole) {
        return new LambdaQueryWrapper<SysRole>()
                .eq(ObjUtil.isNotNull(sysRole.getRoleId()), SysRole::getRoleId, sysRole.getRoleId())
                .eq(ObjUtil.isNotNull(sysRole.getTenantId()), SysRole::getTenantId, sysRole.getTenantId())
                .like(StringUtil.isNotNull(sysRole.getRoleName()), SysRole::getRoleName, sysRole.getRoleName())
                .like(StringUtil.isNotNull(sysRole.getRoleKey()), SysRole::getRoleKey, sysRole.getRoleKey())
                .eq(StringUtil.isNotNull(sysRole.getStatus()), SysRole::getStatus, sysRole.getStatus())
                .orderByDesc(SysRole::getRoleSort, SysRole::getRoleId);
    }

    /**
     * 根据用户ID查询角色
     *
     * @param userId 用户ID
     * @return 角色列表
     */
    @Override
    public List<SysRole> listByUserId(Long userId) {
        List<Long> roleIds = sysUserRoleMapper.listRoleIdByUserId(userId);
        if (CollectionUtils.isEmpty(roleIds)) {
            return Collections.emptyList();
        }
        // 只能查出状态正常的角色
        LambdaQueryWrapper<SysRole> lqw = new LambdaQueryWrapper<SysRole>()
                .in(SysRole::getRoleId, roleIds)
                .eq(SysRole::getStatus, CommonConstants.STATUS_ENABLE);
        return sysRoleMapper.selectListInScope(lqw);
    }

    /**
     * 根据用户id获取角色
     */
    @Override
    public String getRoleGroup(Long userId) {
        // 角色组
        List<SysRole> roles = this.listByUserId(userId);
        return roles.stream().map(SysRole::getRoleName).collect(Collectors.joining(","));
    }

    /**
     * 新增保存角色信息
     *
     * @param roleDTO 角色信息
     * @return 结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int insertRole(SysRoleDTO roleDTO) {
        // 新增角色信息
        int rows = sysRoleMapper.insert(roleDTO);
        // 插入角色菜单信息
        this.insertRoleMenu(roleDTO.getRoleId(), roleDTO.getMenuIds());
        // 新增角色和部门信息（数据权限）
        this.insertRoleDept(roleDTO.getRoleId(), roleDTO.getDeptIds());
        return rows;
    }

    /**
     * 修改保存角色信息
     *
     * @param roleDTO 角色信息
     * @return 结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int updateRole(SysRoleDTO roleDTO) {
        // 删除角色与菜单关联
        sysRoleMenuMapper.deleteByRoleId(roleDTO.getRoleId());
        // 插入角色菜单信息
        this.insertRoleMenu(roleDTO.getRoleId(), roleDTO.getMenuIds());
        // 删除角色与部门关联
        sysRoleDeptMapper.deleteByRoleId(roleDTO.getRoleId());
        // 插入角色和部门信息（数据权限）
        this.insertRoleDept(roleDTO.getRoleId(), roleDTO.getDeptIds());
        // 修改角色信息
        return sysRoleMapper.updateById(roleDTO);
    }

    /**
     * 批量选择授权用户角色
     *
     * @param roleId  角色ID
     * @param userIds 需要授权的用户数据ID
     */
    @Override
    public void allocateUsers(Long roleId, Long[] userIds) {
        if (ObjUtil.isNotEmpty(userIds)) {
            // 新增用户与角色管理
            List<SysUserRole> list = Arrays.stream(userIds)
                    .map(userId -> new SysUserRole(userId, roleId))
                    .toList();
            sysUserRoleMapper.insertBatch(list);
        }
    }

    /**
     * 修改部门数据权限信息
     *
     * @param roleDTO 角色信息
     * @return 结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int authDeptScope(SysRoleDTO roleDTO) {
        // 删除角色与部门关联
        sysRoleDeptMapper.deleteByRoleId(roleDTO.getRoleId());
        // 新增角色和部门信息（数据权限）
        this.insertRoleDept(roleDTO.getRoleId(), roleDTO.getDeptIds());
        // 修改角色信息
        return sysRoleMapper.updateById(roleDTO);
    }

    /**
     * 新增角色菜单信息
     *
     * @param roleId  角色id
     * @param menuIds 菜单id集合
     */
    public void insertRoleMenu(Long roleId, List<Long> menuIds) {
        // 新增用户与角色管理
        if (CollectionUtils.isEmpty(menuIds)) {
            return;
        }
        List<SysRoleMenu> list = menuIds.stream()
                .map(menuId -> new SysRoleMenu(roleId, menuId))
                .toList();
        sysRoleMenuMapper.insertBatch(list);
    }

    /**
     * 新增角色部门信息(数据权限)
     *
     * @param roleId  角色id
     * @param deptIds 部门id集合
     */
    public void insertRoleDept(Long roleId, List<Long> deptIds) {
        if (CollectionUtils.isEmpty(deptIds)) {
            return;
        }
        List<SysRoleDept> list = deptIds.stream()
                .map(deptId -> new SysRoleDept(roleId, deptId))
                .toList();
        sysRoleDeptMapper.insertBatch(list);
    }

    /**
     * 批量删除角色信息
     *
     * @param roleIds 需要删除的角色ID
     * @return 结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int deleteBatchByIds(Long... roleIds) {
        // 删除角色与菜单关联
        sysRoleMenuMapper.deleteByRoleIds(roleIds);
        // 删除角色与部门关联
        sysRoleDeptMapper.deleteByRoleIds(roleIds);
        return sysRoleMapper.deleteByRoleIds(roleIds);
    }

    /**
     * 校验角色权限是否唯一
     *
     * @param roleKey 角色权限
     * @return 结果
     */
    @Override
    public boolean checkRoleKeyUnique(String roleKey, Long roleId) {
        LambdaQueryWrapper<SysRole> lqw = new LambdaQueryWrapper<SysRole>()
                .eq(SysRole::getRoleKey, roleKey)
                .ne(ObjUtil.isNotNull(roleId), SysRole::getRoleId, roleId);
        return sysRoleMapper.exists(lqw);
    }

    /**
     * 校验角色是否被用户关联
     *
     * @param roleIds 角色组
     */
    @Override
    public void checkUserUse(Long[] roleIds) {
        // 是否被用户使用
        if (sysUserRoleMapper.countByRoleIds(roleIds) > 0) {
            throw new BusinessExceptionI18n("business.allocated");
        }
    }

    /**
     * 校验是否有角色的数据权限
     *
     * @param roleIds 待操作的角色id数组
     */
    public void checkDataScopes(Long... roleIds) {
        roleIds = Arrays.stream(roleIds).filter(Objects::nonNull).toArray(Long[]::new);
        if (ObjUtil.isNotEmpty(roleIds)) {
            if (!(sysRoleMapper.checkDataScopes(roleIds) == roleIds.length)) {
                throw new AccessDeniedException("当前部门没有权限访问数据");
            }
        }
    }
}
