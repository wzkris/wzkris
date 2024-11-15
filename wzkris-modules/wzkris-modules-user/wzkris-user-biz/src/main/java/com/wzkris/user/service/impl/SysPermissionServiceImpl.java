package com.wzkris.user.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.wzkris.common.core.utils.StringUtil;
import com.wzkris.common.orm.annotation.DynamicTenant;
import com.wzkris.user.api.domain.dto.SysPermissionDTO;
import com.wzkris.user.domain.SysDept;
import com.wzkris.user.domain.SysRole;
import com.wzkris.user.domain.SysUser;
import com.wzkris.user.mapper.SysDeptMapper;
import com.wzkris.user.mapper.SysRoleDeptMapper;
import com.wzkris.user.mapper.SysTenantMapper;
import com.wzkris.user.mapper.SysTenantPackageMapper;
import com.wzkris.user.service.SysMenuService;
import com.wzkris.user.service.SysPermissionService;
import com.wzkris.user.service.SysRoleService;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 用户权限处理
 *
 * @author wzkris
 */
@Service
@RequiredArgsConstructor
public class SysPermissionServiceImpl implements SysPermissionService {
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

    private final SysRoleService roleService;
    private final SysMenuService menuService;
    private final SysDeptMapper deptMapper;
    private final SysRoleDeptMapper sysRoleDeptMapper;
    private final SysTenantMapper tenantMapper;
    private final SysTenantPackageMapper sysTenantPackageMapper;

    @Override
    @DynamicTenant(value = "#tenantId", parseType = DynamicTenant.ParseType.SPEL_NUMBER)
    public SysPermissionDTO getPermission(@Nonnull Long userId, @Nonnull Long tenantId, @Nullable Long deptId) {
        List<SysRole> roles;
        List<String> grantedAuthority;
        List<Long> deptScopes = Collections.emptyList();
        boolean administrator;
        if (SysUser.isSuperAdmin(userId)) {
            // 超级管理员查出所有角色
            administrator = true;
            grantedAuthority = Collections.singletonList("*");
        }
        else {
            // 租户最高管理员特殊处理
            Long tenantPackageId = tenantMapper.selectPackageIdByUserId(userId);
            if (tenantPackageId != null) {
                // 租户最高管理员查出所有租户角色
                administrator = true;
                // 查出套餐绑定的所有权限
                List<Long> menuIds = sysTenantPackageMapper.listMenuIdByPackageId(tenantPackageId);
                grantedAuthority = menuService.listPermsByMenuIds(menuIds);
            }
            else {
                // 否则为普通用户
                administrator = false;
                roles = roleService.listByUserId(userId);
                // 菜单权限
                List<Long> roleIds = roles.stream().map(SysRole::getRoleId).collect(Collectors.toList());
                grantedAuthority = menuService.listPermsByRoleIds(roleIds);
                // 数据权限
                deptScopes = this.listDeptScope(roles, deptId);
            }
        }
        return new SysPermissionDTO(administrator, grantedAuthority, deptScopes);
    }

    /**
     * 根据角色集合查询数据权限（可访问的部门id集合）
     *
     * @param roles  角色集合
     * @param deptId 自身归属的部门id
     * @return 部门id集合
     */
    private List<Long> listDeptScope(List<SysRole> roles, Long deptId) {
        // 若部门id为空或者无角色，则代表不存在数据权限
        if (deptId == null || CollectionUtils.isEmpty(roles)) {
            return Collections.emptyList();
        }
        Set<Long> deptIds = new HashSet<>();
        // 循环每一个角色，拼接所有可访问的部门id
        Map<String, List<SysRole>> datascopeMap = roles
                .stream()
                // 根据权限作用域分组
                .collect(Collectors.groupingBy(SysRole::getDataScope));
        for (Map.Entry<String, List<SysRole>> entry : datascopeMap.entrySet()) {
            if (StringUtil.equals(DATA_SCOPE_ALL, entry.getKey())) {
                deptIds = deptMapper.selectList(Wrappers.lambdaQuery(SysDept.class).select(SysDept::getDeptId))
                        .stream()
                        .map(SysDept::getDeptId)
                        .collect(Collectors.toSet());
                break;
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
                List<Long> addDeptIds = deptMapper.listChildrenIdById(deptId);
                deptIds.addAll(addDeptIds);
            }
            else if (StringUtil.equals(DATA_SCOPE_SELF, entry.getKey())) {
                // 本人数据权限
                deptIds.add(-999L);
            }
        }
        return deptIds.stream().toList();
    }

}
