package com.wzkris.user.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.wzkris.common.core.constant.SecurityConstants;
import com.wzkris.common.core.utils.StringUtil;
import com.wzkris.common.orm.utils.DynamicTenantUtil;
import com.wzkris.user.domain.DeptInfoDO;
import com.wzkris.user.domain.RoleInfoDO;
import com.wzkris.user.domain.UserInfoDO;
import com.wzkris.user.feign.userinfo.resp.PermissionResp;
import com.wzkris.user.manager.DeptInfoDataScopeManager;
import com.wzkris.user.mapper.DeptInfoMapper;
import com.wzkris.user.mapper.TenantInfoMapper;
import com.wzkris.user.service.MenuInfoService;
import com.wzkris.user.service.PermissionService;
import com.wzkris.user.service.RoleInfoService;
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
public class PermissionServiceImpl implements PermissionService {

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

    private final RoleInfoService roleInfoService;

    private final MenuInfoService menuInfoService;

    private final DeptInfoMapper deptInfoMapper;

    private final TenantInfoMapper tenantInfoMapper;

    private final DeptInfoDataScopeManager deptInfoDataScopeManager;

    @Override
    public PermissionResp getPermission(Long userId, Long tenantId, Long deptId) {
        return DynamicTenantUtil.switcht(tenantId, () -> {
            List<RoleInfoDO> roles;
            List<String> grantedAuthority;
            List<Long> deptScopes = Collections.emptyList();
            boolean administrator = false;
            if (UserInfoDO.isSuperAdmin(userId)) {
                // 超级管理员查出所有角色
                administrator = true;
                grantedAuthority = Collections.singletonList(SecurityConstants.SUPER_PERMISSION);
            } else {
                // 租户最高管理员特殊处理
                Long tenantPackageId = tenantInfoMapper.selectPackageIdByUserId(userId);
                if (tenantPackageId != null) {
                    // 租户最高管理员查出所有租户角色
                    administrator = true;
                    grantedAuthority = menuInfoService.listPermsByTenantPackageId(tenantPackageId);
                } else {
                    // 否则为普通用户
                    roles = roleInfoService.listInheritedByUserId(userId);
                    // 菜单权限
                    List<Long> roleIds = roles.stream().map(RoleInfoDO::getRoleId).collect(Collectors.toList());
                    grantedAuthority = menuInfoService.listPermsByRoleIds(roleIds);
                    // 数据权限
                    deptScopes = this.listDeptScope(roles, deptId);
                }
            }
            return new PermissionResp(administrator, grantedAuthority, deptScopes);
        });
    }

    /**
     * 根据角色集合查询数据权限（可访问的部门id集合）
     *
     * @param roles  角色集合
     * @param deptId 自身归属的部门id
     * @return 部门id集合
     */
    private List<Long> listDeptScope(List<RoleInfoDO> roles, Long deptId) {
        // 若部门id为空或者无角色，则代表不存在数据权限
        if (deptId == null || CollectionUtils.isEmpty(roles)) {
            return Collections.singletonList(-999L);
        }
        Set<Long> deptIds = new HashSet<>();
        // 循环每一个角色，拼接所有可访问的部门id
        Map<String, List<Long>> datascopeMap = roles.stream()
                // 根据权限作用域分组
                .collect(Collectors.groupingBy(
                        RoleInfoDO::getDataScope, Collectors.mapping(RoleInfoDO::getRoleId, Collectors.toList())));
        for (Map.Entry<String, List<Long>> entry : datascopeMap.entrySet()) {
            if (StringUtil.equals(DATA_SCOPE_ALL, entry.getKey())) {
                deptIds = deptInfoMapper
                        .selectList(Wrappers.lambdaQuery(DeptInfoDO.class).select(DeptInfoDO::getDeptId))
                        .stream()
                        .map(DeptInfoDO::getDeptId)
                        .collect(Collectors.toSet());
                break;
            } else if (StringUtil.equals(DATA_SCOPE_CUSTOM, entry.getKey())) {
                // 自定义部门权限
                deptIds.addAll(deptInfoDataScopeManager.listDeptIdByRoleIds(entry.getValue()));
            } else if (StringUtil.equals(DATA_SCOPE_DEPT, entry.getKey())) {
                // 部门自身数据权限
                deptIds.add(deptId);
            } else if (StringUtil.equals(DATA_SCOPE_DEPT_AND_CHILD, entry.getKey())) {
                // 部门及以下数据权限
                deptIds.addAll(deptInfoMapper.listSubDeptIdById(deptId));
            } else {
                // 本人数据权限
                deptIds.add(-999L);
            }
        }
        return deptIds.stream().toList();
    }

}
