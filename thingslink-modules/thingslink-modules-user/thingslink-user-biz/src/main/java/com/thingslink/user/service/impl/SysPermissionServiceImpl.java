package com.thingslink.user.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.thingslink.common.core.constant.CommonConstants;
import com.thingslink.common.core.utils.StringUtil;
import com.thingslink.common.orm.annotation.DynamicTenant;
import com.thingslink.user.api.domain.dto.SysPermissionDTO;
import com.thingslink.user.domain.SysMenu;
import com.thingslink.user.domain.SysRole;
import com.thingslink.user.domain.SysUser;
import com.thingslink.user.mapper.*;
import com.thingslink.user.service.SysMenuService;
import com.thingslink.user.service.SysPermissionService;
import com.thingslink.user.service.SysRoleService;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 用户权限处理
 *
 * @author wzkris
 */
@Service
@RequiredArgsConstructor
public class SysPermissionServiceImpl implements SysPermissionService {
    private final SysRoleMapper sysRoleMapper;
    private final SysRoleService sysRoleService;
    private final SysMenuMapper sysMenuMapper;
    private final SysMenuService sysMenuService;
    private final SysDeptMapper sysDeptMapper;
    private final SysRoleDeptMapper sysRoleDeptMapper;
    private final SysTenantMapper sysTenantMapper;
    private final SysTenantPackageMapper sysTenantPackageMapper;

    /**
     * 全部数据权限
     */
    public static final String DATA_SCOPE_ALL = "1";

    /**
     * 自定数据权限
     */
    public static final String DATA_SCOPE_CUSTOM = "2";

    /**
     * 部门数据权限
     */
    public static final String DATA_SCOPE_DEPT = "3";

    /**
     * 部门及以下数据权限
     */
    public static final String DATA_SCOPE_DEPT_AND_CHILD = "4";

    /**
     * 仅本人数据权限
     */
    public static final String DATA_SCOPE_SELF = "5";

    /**
     * 返回已授权码及数据权限
     *
     * @param userId   用户ID
     * @param tenantId 租户ID
     * @param deptId   部门ID
     * @return 权限
     */
    @Override
    @DynamicTenant(value = "#tenantId")
    public SysPermissionDTO getPermission(@Nonnull Long userId, @Nonnull Long tenantId, @Nullable Long deptId) {
        List<SysRole> roles;
        List<String> grantedAuthority;
        List<Long> deptScopes = Collections.emptyList();
        boolean isAdmin = false;
        if (SysUser.isSuperAdmin(userId)) {
            // 超级管理员查出所有角色
            isAdmin = true;
            grantedAuthority = Collections.singletonList("*");
        }
        else {
            // 租户最高管理员特殊处理
            Long tenantPackageId = sysTenantMapper.selectPackageIdByUserId(userId);
            if (tenantPackageId != null) {
                // 租户最高管理员查出所有租户角色
                isAdmin = true;
                grantedAuthority = sysRoleMapper.selectList(null).stream()
                        .map(SysRole::getRoleKey)
                        .filter(StringUtil::isNotBlank)
                        .collect(Collectors.toList());
                // 查出套餐绑定的所有权限
                List<Long> menuIds = sysTenantPackageMapper.listMenuIdByPackageId(tenantPackageId);
                List<String> menuPerms = sysMenuMapper.selectList(new LambdaQueryWrapper<SysMenu>()
                        .select(SysMenu::getPerms)
                        .eq(SysMenu::getStatus, CommonConstants.STATUS_ENABLE)
                        .in(SysMenu::getMenuId, menuIds)
                ).stream().map(SysMenu::getPerms).filter(StringUtil::isNotBlank).toList();
                grantedAuthority.addAll(menuPerms);
            }
            else {
                // 否则为普通用户
                roles = sysRoleService.listByUserId(userId);
                grantedAuthority = roles.stream()
                        .map(SysRole::getRoleKey)
                        .filter(StringUtil::isNotBlank)
                        .collect(Collectors.toList());
                // 菜单权限
                List<Long> roleIds = roles.stream().map(SysRole::getRoleId).collect(Collectors.toList());
                grantedAuthority.addAll(sysMenuService.listPermsByRoleIds(roleIds).stream().filter(StringUtil::isNotBlank).toList());
                // 数据权限
                deptScopes = this.listDeptScope(roles, deptId);
            }
        }
        return new SysPermissionDTO(isAdmin, grantedAuthority, deptScopes);
    }

    /**
     * 根据角色集合查询数据权限（可访问的部门id集合）
     *
     * @param roles  角色集合
     * @param deptId 自身归属的部门id
     * @return 部门id集合
     */
    public List<Long> listDeptScope(List<SysRole> roles, Long deptId) {
        // 若部门id为空或者无角色，则代表不存在数据权限
        if (deptId == null || CollectionUtils.isEmpty(roles)) {
            return Collections.emptyList();
        }
        List<Long> deptIds = new ArrayList<>();
        // 循环每一个角色，拼接所有可访问的部门id
        Map<String, List<SysRole>> datascopeMap = roles
                .stream()
                // 根据权限作用域分组
                .collect(Collectors.groupingBy(SysRole::getDataScope));
        for (Map.Entry<String, List<SysRole>> entry : datascopeMap.entrySet()) {
            if (StringUtil.equals(DATA_SCOPE_ALL, entry.getKey())) {
                continue;
            }
            else if (StringUtil.equals(DATA_SCOPE_CUSTOM, entry.getKey())) {
                // 自定义部门权限
                List<Long> roleIds = entry.getValue().stream().map(SysRole::getRoleId).collect(Collectors.toList());
                List<Long> addDeptIds = sysRoleDeptMapper.listDeptIdByRoleIds(roleIds);
                deptIds.addAll(addDeptIds);
            }
            else if (StringUtil.equals(DATA_SCOPE_DEPT, entry.getKey())) {
                // 部门自身数据权限
                deptIds.add(deptId);
            }
            else if (StringUtil.equals(DATA_SCOPE_DEPT_AND_CHILD, entry.getKey())) {
                // 部门及以下数据权限
                List<Long> addDeptIds = sysDeptMapper.listChildrenIdById(deptId);
                deptIds.addAll(addDeptIds);
            }
            if (StringUtil.equals(DATA_SCOPE_SELF, entry.getKey())) {
                // 本人数据权限
                deptIds.add(-999L);
            }
        }
        return deptIds;
    }

}
