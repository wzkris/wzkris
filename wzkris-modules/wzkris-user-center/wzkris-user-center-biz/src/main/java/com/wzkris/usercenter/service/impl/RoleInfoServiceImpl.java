package com.wzkris.usercenter.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wzkris.common.core.utils.StringUtil;
import com.wzkris.usercenter.domain.RoleInfoDO;
import com.wzkris.usercenter.domain.RoleInheritanceDO;
import com.wzkris.usercenter.domain.RoleToDeptDO;
import com.wzkris.usercenter.domain.RoleToMenuDO;
import com.wzkris.usercenter.enums.role.RoleStatusEnum;
import com.wzkris.usercenter.mapper.*;
import com.wzkris.usercenter.response.common.SelectResponse;
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
public class RoleInfoServiceImpl
        extends ServiceImpl<RoleInfoMapper, RoleInfoDO>
        implements RoleInfoService {

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
                .eq(RoleInfoDO::getStatus, RoleStatusEnum.ENABLE);
        return baseMapper.selectList(lqw);
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
                .eq(RoleInfoDO::getStatus, RoleStatusEnum.ENABLE);
        return baseMapper.selectList(lqw).stream().map(RoleInfoDO::getRoleId).toList();
    }

    @Override
    public String getRoleGroup(Long adminId) {
        List<RoleInfoDO> roles = this.listByAdminId(adminId);
        return roles.stream().map(RoleInfoDO::getRoleName).collect(Collectors.joining(","));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean saveRole(RoleInfoDO role, List<Long> menuIds, List<Long> deptIds, List<Long> childIds) {
        // 新增角色信息
        boolean success = baseMapper.insert(role) > 0;
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
    public boolean updateRole(RoleInfoDO role, List<Long> menuIds, List<Long> deptIds, List<Long> childIds) {
        // 修改角色信息
        boolean success = baseMapper.updateById(role) > 0;
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
    public boolean removeRoles(List<Long> roleIds) {
        boolean success = baseMapper.deleteByIds(roleIds) > 0;
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

    @Override
    public List<SelectResponse> listRoleSelect(String roleName) {
        List<RoleInfoDO> allRoles = this.lambdaQuery()
                .like(StringUtil.isNotBlank(roleName), RoleInfoDO::getRoleName, roleName)
                .list();
        return allRoles.stream()
                .map(roleInfoDO -> {
                    SelectResponse selectResponse = new SelectResponse();
                    selectResponse.setId(roleInfoDO.getRoleId());
                    selectResponse.setLabel(roleInfoDO.getRoleName());
                    return selectResponse;
                })
                .toList();
    }

}
