package com.wzkris.usercenter.serviceimpl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.wzkris.common.core.constant.CommonConstants;
import com.wzkris.common.core.constant.SecurityConstants;
import com.wzkris.common.security.utils.SecurityUtil;
import com.wzkris.usercenter.domain.*;
import com.wzkris.usercenter.mapper.*;
import com.wzkris.usercenter.service.RoleInfoService;
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

    private final AdminToRoleMapper adminToRoleMapper;

    private final RoleToDeptMapper roleToDeptMapper;

    private final RoleInheritanceMapper roleInheritanceMapper;

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
     * 根据管理员 ID 获取角色 ID 列表（可选包含继承角色）
     *
     * @param adminId          管理员 ID
     * @param includeInherited 是否包含继承角色
     * @return 角色 ID 列表
     */
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

    /**
     * 根据角色 ID 列表查询角色信息（仅返回正常状态的）
     *
     * @param roleIds 角色 ID 列表
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
     * 根据角色 ID 列表查询角色 ID 列表（仅返回正常状态的）
     *
     * @param roleIds 角色 ID 列表
     * @return 角色 ID 列表
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
        if (SecurityUtil.isSuper()) {
            return SecurityConstants.SUPER_ADMIN_NAME;
        }
        List<RoleInfoDO> roles = this.listByAdminId(SecurityUtil.getUid());
        return roles.stream().map(RoleInfoDO::getRoleName).collect(Collectors.joining(","));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean saveRole(RoleInfoDO role, List<Long> menuIds, List<Long> deptIds, List<Long> childIds) {
        // 新增角色信息
        boolean success = roleInfoMapper.insert(role) > 0;
        if (success) {
            // 新增角色菜单信息
            this.insertRoleMenu(role.getRoleId(), menuIds);
            // 新增角色和部门信息（数据权限）
            this.insertRoleDept(role.getRoleId(), deptIds);
            // 新增角色继承关系
            this.insertRoleInheritance(role.getRoleId(), childIds);
        }
        return success;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean modifyRole(RoleInfoDO role, List<Long> menuIds, List<Long> deptIds, List<Long> childIds) {
        // 修改角色信息
        boolean success = roleInfoMapper.updateById(role) > 0;
        if (success) {
            // 删除并重新设置角色继承关系
            if (childIds != null) {
                roleInheritanceMapper.deleteByRoleId(role.getRoleId());
                this.insertRoleInheritance(role.getRoleId(), childIds);
            }
            // 删除角色与菜单关联
            if (menuIds != null) {
                roleToMenuMapper.deleteByRoleId(role.getRoleId());
                this.insertRoleMenu(role.getRoleId(), menuIds);
            }
            // 删除角色与部门关联
            if (deptIds != null) {
                roleToDeptMapper.deleteByRoleId(role.getRoleId());
                this.insertRoleDept(role.getRoleId(), deptIds);
            }
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
     * @param roleId  角色 id
     * @param menuIds 菜单 id 集合
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
     * @param roleId  角色 id
     * @param deptIds 部门 id 集合
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
            // 删除角色继承关系（作为角色和子角色）
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

    /**
     * 新增角色继承关系
     *
     * @param roleId   角色 ID
     * @param childIds 子角色 ID 列表
     */
    private void insertRoleInheritance(Long roleId, List<Long> childIds) {
        if (CollectionUtils.isNotEmpty(childIds)) {
            List<RoleInheritanceDO> list = childIds.stream()
                    .map(childId -> new RoleInheritanceDO(roleId, childId))
                    .toList();
            roleInheritanceMapper.insert(list);
        }
    }

}
