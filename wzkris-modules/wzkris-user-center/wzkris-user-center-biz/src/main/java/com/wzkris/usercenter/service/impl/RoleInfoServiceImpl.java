package com.wzkris.usercenter.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.wzkris.common.core.constant.CommonConstants;
import com.wzkris.common.core.constant.SecurityConstants;
import com.wzkris.common.security.utils.AdminUtil;
import com.wzkris.usercenter.domain.AdminToRoleDO;
import com.wzkris.usercenter.domain.RoleInfoDO;
import com.wzkris.usercenter.domain.RoleToDeptDO;
import com.wzkris.usercenter.domain.RoleToMenuDO;
import com.wzkris.usercenter.mapper.AdminToRoleMapper;
import com.wzkris.usercenter.mapper.RoleInfoMapper;
import com.wzkris.usercenter.mapper.RoleToDeptMapper;
import com.wzkris.usercenter.mapper.RoleToMenuMapper;
import com.wzkris.usercenter.service.RoleInfoService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
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

    private final AdminToRoleMapper adminToRoleMapper;

    private final RoleToDeptMapper roleToDeptMapper;

    @Override
    public List<RoleInfoDO> listByAdminId(Long adminId) {
        List<Long> roleIds = listRoleIdsByAdminId(adminId, false);
        return listByIds(roleIds);
    }

    @Override
    public List<Long> listIdByAdminId(Long adminId) {
        List<Long> roleIds = listRoleIdsByAdminId(adminId, false);
        return listRoleIdsByIds(roleIds);
    }

    @Override
    public List<RoleInfoDO> listInheritedByAdminId(Long adminId) {
        List<Long> roleIds = listRoleIdsByAdminId(adminId, true);
        return listByIds(roleIds);
    }

    @Override
    public List<Long> listInheritedIdByAdminId(Long adminId) {
        List<Long> roleIds = listRoleIdsByAdminId(adminId, true);
        return listRoleIdsByIds(roleIds);
    }

    /**
     * 根据管理员ID获取角色ID列表（可选包含继承角色）
     *
     * @param adminId          管理员ID
     * @param includeInherited 是否包含继承角色
     * @return 角色ID列表
     */
    private List<Long> listRoleIdsByAdminId(Long adminId, boolean includeInherited) {
        List<Long> roleIds = adminToRoleMapper.listRoleIdByAdminId(adminId);
        if (CollectionUtils.isEmpty(roleIds)) {
            return Collections.emptyList();
        }
        if (includeInherited) {
            List<Long> childRoleIds = getChildrenIdsByRoleIds(roleIds);
            if (CollectionUtils.isNotEmpty(childRoleIds)) {
                roleIds.addAll(childRoleIds);
            }
        }
        return roleIds;
    }

    /**
     * 根据角色ID列表获取所有子角色ID（从 childrenId 字段，递归查询所有层级的子角色）
     *
     * @param roleIds 角色ID列表
     * @return 所有子角色ID列表（包含直接和间接子角色）
     */
    private List<Long> getChildrenIdsByRoleIds(List<Long> roleIds) {
        if (CollectionUtils.isEmpty(roleIds)) {
            return Collections.emptyList();
        }
        // 使用 Set 收集所有子角色ID，自动去重
        Set<Long> allChildrenIds = new HashSet<>();
        // 使用 Set 记录已访问的角色ID，防止循环依赖导致无限递归
        Set<Long> visitedRoleIds = new HashSet<>(roleIds);
        // 递归查询所有子角色
        getChildrenIdsRecursive(roleIds, allChildrenIds, visitedRoleIds);
        return new ArrayList<>(allChildrenIds);
    }

    /**
     * 递归查询所有子角色ID
     *
     * @param roleIds        当前层级的角色ID列表
     * @param allChildrenIds 收集所有子角色ID的集合
     * @param visitedRoleIds 已访问的角色ID集合，用于防止循环依赖
     */
    private void getChildrenIdsRecursive(List<Long> roleIds, Set<Long> allChildrenIds, Set<Long> visitedRoleIds) {
        if (CollectionUtils.isEmpty(roleIds)) {
            return;
        }
        // 查询当前层级的直接子角色
        LambdaQueryWrapper<RoleInfoDO> lqw = new LambdaQueryWrapper<RoleInfoDO>()
                .select(RoleInfoDO::getChildrenId)
                .in(RoleInfoDO::getRoleId, roleIds)
                .isNotNull(RoleInfoDO::getChildrenId);
        List<RoleInfoDO> roles = roleInfoMapper.selectList(lqw);

        // 提取当前层级的子角色ID
        List<Long> currentLevelChildren = roles.stream()
                .filter(role -> role.getChildrenId() != null && role.getChildrenId().length > 0)
                .flatMap(role -> Arrays.stream(role.getChildrenId()))
                .filter(Objects::nonNull)
                .distinct()
                .collect(Collectors.toList());

        if (CollectionUtils.isEmpty(currentLevelChildren)) {
            return;
        }

        // 过滤掉已访问的角色，避免循环依赖
        List<Long> newChildren = currentLevelChildren.stream()
                .filter(childId -> !visitedRoleIds.contains(childId))
                .collect(Collectors.toList());

        if (CollectionUtils.isEmpty(newChildren)) {
            return;
        }

        // 将新的子角色ID添加到结果集和已访问集合
        allChildrenIds.addAll(newChildren);
        visitedRoleIds.addAll(newChildren);

        // 递归查询下一层级的子角色
        getChildrenIdsRecursive(newChildren, allChildrenIds, visitedRoleIds);
    }

    /**
     * 根据角色ID列表查询角色信息（仅返回正常状态的）
     *
     * @param roleIds 角色ID列表
     * @return 角色列表
     */
    private List<RoleInfoDO> listByIds(List<Long> roleIds) {
        if (CollectionUtils.isEmpty(roleIds)) {
            return Collections.emptyList();
        }
        LambdaQueryWrapper<RoleInfoDO> lqw = new LambdaQueryWrapper<RoleInfoDO>()
                .in(RoleInfoDO::getRoleId, roleIds)
                .eq(RoleInfoDO::getStatus, CommonConstants.STATUS_ENABLE);
        return roleInfoMapper.selectList(lqw);
    }

    /**
     * 根据角色ID列表查询角色ID列表（仅返回正常状态的）
     *
     * @param roleIds 角色ID列表
     * @return 角色ID列表
     */
    private List<Long> listRoleIdsByIds(List<Long> roleIds) {
        if (CollectionUtils.isEmpty(roleIds)) {
            return Collections.emptyList();
        }
        LambdaQueryWrapper<RoleInfoDO> lqw = new LambdaQueryWrapper<RoleInfoDO>()
                .select(RoleInfoDO::getRoleId)
                .in(RoleInfoDO::getRoleId, roleIds)
                .eq(RoleInfoDO::getStatus, CommonConstants.STATUS_ENABLE);
        return roleInfoMapper.selectList(lqw).stream().map(RoleInfoDO::getRoleId).toList();
    }

    @Override
    public String getRoleGroup() {
        if (AdminUtil.isSuperadmin()) {
            return SecurityConstants.SUPER_ADMIN_NAME;
        }
        List<RoleInfoDO> roles = this.listByAdminId(AdminUtil.getId());
        return roles.stream().map(RoleInfoDO::getRoleName).collect(Collectors.joining(","));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean saveRole(RoleInfoDO role, List<Long> menuIds, List<Long> deptIds) {
        // 新增角色信息
        boolean success = roleInfoMapper.insert(role) > 0;
        if (success) {
            // 新增角色菜单信息
            this.insertRoleMenu(role.getRoleId(), menuIds);
            // 新增角色和部门信息（数据权限）
            this.insertRoleDept(role.getRoleId(), deptIds);
        }
        return success;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean modifyRole(RoleInfoDO role, List<Long> menuIds, List<Long> deptIds) {
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
        return success;
    }

    @Override
    public boolean grantAdmins(Long roleId, List<Long> adminIds) {
        if (CollectionUtils.isNotEmpty(adminIds)) {
            // 新增用户与角色管理
            List<AdminToRoleDO> list = adminIds.stream()
                    .map(adminId -> new AdminToRoleDO(adminId, roleId))
                    .toList();
            return adminToRoleMapper.insert(list) > 0;
        }
        return false;
    }

    @Override
    public boolean ungrantAdmins(Long roleId, List<Long> adminIds) {
        if (CollectionUtils.isNotEmpty(adminIds)) {
            return adminToRoleMapper.deleteBatch(roleId, adminIds) > 0;
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
            roleToMenuMapper.insert(list);
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
            List<RoleToDeptDO> list = deptIds.stream()
                    .map(deptId -> new RoleToDeptDO(roleId, deptId))
                    .toList();
            roleToDeptMapper.insert(list);
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
            adminToRoleMapper.deleteByRoleIds(roleIds);
        }
        return success;
    }

    @Override
    public boolean existAdmin(List<Long> roleIds) {
        roleIds = roleIds.stream().filter(Objects::nonNull).toList();
        // 是否被用户使用
        return adminToRoleMapper.existByRoleIds(roleIds);
    }

    @Override
    public boolean checkIsChildren(List<Long> roleIds) {
        if (CollectionUtils.isEmpty(roleIds)) {
            return false;
        }
        // 检查是否有其他角色的 childrenId 包含这些角色ID
        LambdaQueryWrapper<RoleInfoDO> lqw = new LambdaQueryWrapper<RoleInfoDO>()
                .select(RoleInfoDO::getRoleId, RoleInfoDO::getChildrenId)
                .isNotNull(RoleInfoDO::getChildrenId);
        List<RoleInfoDO> roles = roleInfoMapper.selectList(lqw);
        return roles.stream()
                .filter(role -> role.getChildrenId() != null && role.getChildrenId().length > 0)
                .anyMatch(role -> {
                    List<Long> childrenIds = Arrays.asList(role.getChildrenId());
                    return roleIds.stream().anyMatch(childrenIds::contains);
                });
    }

}
