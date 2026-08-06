package com.wzkris.usercenter.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.wzkris.common.core.constant.SecurityConstants;
import com.wzkris.common.core.model.DataIdentity;
import com.wzkris.common.core.model.UserRole;
import com.wzkris.usercenter.domain.*;
import com.wzkris.usercenter.enums.role.DataScopeEnum;
import com.wzkris.usercenter.mapper.DeptInfoMapper;
import com.wzkris.usercenter.mapper.RoleToDeptMapper;
import com.wzkris.usercenter.service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 用户权限处理
 *
 * @author wzkris
 */
@Service
@RequiredArgsConstructor
public class PermissionServiceImpl implements PermissionService {

    private final RoleInfoService roleInfoService;

    private final MenuInfoService menuInfoService;

    private final DeptInfoMapper deptInfoMapper;

    private final RoleToDeptMapper roleToDeptMapper;

    private final PostInfoService postInfoService;

    private final TenantInfoService tenantInfoService;

    @Override
    public List<UserRole> getAdminPermission(Long adminId, Long deptId) {
        // 超管：加载全部真实权限码 + 虚拟 ALL 角色跳过数据权限
        if (AdminInfoDO.isSuperAdmin(adminId)) {
            return List.of(
                    new UserRole(0L, SecurityConstants.SUPER_ADMIN_NAME, DataScopeEnum.ALL.getValue(),
                            Collections.emptyList(), menuInfoService.listPermsByMenuIds(null))
            );
        }

        // 普通用户：查角色 -> 查菜单权限 -> 构建角色数据权限
        List<RoleInfoDO> roleList = roleInfoService.listByAdminId(adminId, true);

        return roleList.stream()
                .map(role -> new UserRole(
                        role.getId(),
                        role.getRoleName(),
                        role.getDataScope().getValue(),
                        computeDataIdentities(role, deptId),
                        menuInfoService.listPermsByRoleIds(List.of(role.getId()))
                ))
                .collect(Collectors.toList());
    }

    @Override
    public List<UserRole> getTenantPermission(Long memberId, Long tenantId) {
        List<UserRole> roles;
        // 租户最高管理员特殊处理
        Long tenantPackageId = tenantInfoService.getObjByObj(TenantInfoDO::getPackageId,
                TenantInfoDO::getAdministrator, memberId);
        if (tenantPackageId != null) {
            roles = List.of(new UserRole(0L, SecurityConstants.SUPER_ADMIN_NAME, null, null,
                    menuInfoService.listPermsByTenantPackageId(tenantPackageId)));
        } else {
            List<PostInfoDO> posts = postInfoService.listByMemberId(memberId);
            roles = posts.stream()
                    .map(post -> new UserRole(post.getId(), post.getPostName(), null, null,
                            menuInfoService.listPermsByPostIds(List.of(post.getId()))))
                    .collect(Collectors.toList());
        }
        return roles;
    }

    /**
     * 根据角色数据范围计算可访问的数据标识列表
     */
    private List<DataIdentity> computeDataIdentities(RoleInfoDO role, Long deptId) {
        DataScopeEnum scope = role.getDataScope();
        if (scope == null) {
            return Collections.emptyList();
        }

        return switch (scope) {
            case ALL -> Collections.emptyList();
            case CUSTOM -> toDataIdentities(
                    roleToDeptMapper.listDeptIdByRoleIds(List.of(role.getId()))
            );
            case DEPT -> deptId != null
                    ? toDataIdentities(List.of(deptId))
                    : Collections.emptyList();
            case DEPT_AND_CHILD -> deptId != null
                    ? toDataIdentities(deptInfoMapper.listSubDeptIdById(deptId))
                    : Collections.emptyList();
            case ONLY_SELF -> List.of(new DataIdentity(-999L, "本人"));
        };
    }

    /**
     * 批量查询部门名称，转换为 DataIdentity 列表
     */
    private List<DataIdentity> toDataIdentities(List<Long> deptIds) {
        if (CollectionUtils.isEmpty(deptIds)) {
            return Collections.emptyList();
        }
        return deptInfoMapper.selectList(
                        Wrappers.lambdaQuery(DeptInfoDO.class)
                                .select(DeptInfoDO::getId, DeptInfoDO::getDeptName)
                                .in(DeptInfoDO::getId, deptIds)
                ).stream()
                .map(d -> new DataIdentity(d.getId(), d.getDeptName()))
                .collect(Collectors.toList());
    }

}
