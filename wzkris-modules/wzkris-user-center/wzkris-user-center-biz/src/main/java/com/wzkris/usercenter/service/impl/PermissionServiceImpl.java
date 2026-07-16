package com.wzkris.usercenter.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.wzkris.common.core.constant.SecurityConstants;
import com.wzkris.common.core.model.DataIdentity;
import com.wzkris.common.core.model.UserRole;
import com.wzkris.usercenter.domain.*;
import com.wzkris.usercenter.enums.role.DataScopeEnum;
import com.wzkris.usercenter.mapper.DeptInfoMapper;
import com.wzkris.usercenter.mapper.RoleToDeptMapper;
import com.wzkris.usercenter.remote.api.admin.response.AdminPermissionResponse;
import com.wzkris.usercenter.remote.api.member.response.MemberPermissionResponse;
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
    public AdminPermissionResponse getAdminPermission(Long adminId, Long deptId) {
        // 超管：加载全部真实权限码 + 虚拟 ALL 角色跳过数据权限
        if (AdminInfoDO.isSuperAdmin(adminId)) {
            List<UserRole> roles = List.of(
                    new UserRole(0L, SecurityConstants.SUPER_ADMIN_NAME, DataScopeEnum.ALL.getValue(), Collections.emptyList())
            );
            return new AdminPermissionResponse(menuInfoService.listPermsByMenuIds(null), roles);
        }

        // 普通用户：查角色 -> 查菜单权限 -> 构建角色数据权限
        List<RoleInfoDO> roleList = roleInfoService.listByAdminId(adminId, true);
        List<Long> roleIds = roleList.stream().map(RoleInfoDO::getRoleId).collect(Collectors.toList());
        List<String> grantedAuthority = menuInfoService.listPermsByRoleIds(roleIds);

        List<UserRole> roles = roleList.stream()
                .map(role -> new UserRole(
                        role.getRoleId(),
                        role.getRoleName(),
                        role.getDataScope().getValue(),
                        computeDataIdentities(role, deptId)
                ))
                .collect(Collectors.toList());

        return new AdminPermissionResponse(grantedAuthority, roles);
    }

    @Override
    public MemberPermissionResponse getTenantPermission(Long memberId, Long tenantId) {
        List<String> grantedAuthority;
        List<UserRole> roles;
        boolean administrator = false;
        // 租户最高管理员特殊处理
        Long tenantPackageId = tenantInfoService.getObjByObj(TenantInfoDO::getPackageId,
                TenantInfoDO::getAdministrator, memberId);
        if (tenantPackageId != null) {
            administrator = true;
            grantedAuthority = menuInfoService.listPermsByTenantPackageId(tenantPackageId);
            roles = List.of(new UserRole(0L, SecurityConstants.SUPER_ADMIN_NAME, null, null));
        } else {
            List<PostInfoDO> posts = postInfoService.listByMemberId(memberId);
            List<Long> postIds = posts.stream().map(PostInfoDO::getPostId).collect(Collectors.toList());
            grantedAuthority = menuInfoService.listPermsByPostIds(postIds);
            roles = posts.stream()
                    .map(post -> new UserRole(post.getPostId(), post.getPostName(), null, null))
                    .collect(Collectors.toList());
        }
        return new MemberPermissionResponse(administrator, grantedAuthority, roles);
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
                    roleToDeptMapper.listDeptIdByRoleIds(List.of(role.getRoleId()))
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
                                .select(DeptInfoDO::getDeptId, DeptInfoDO::getDeptName)
                                .in(DeptInfoDO::getDeptId, deptIds)
                ).stream()
                .map(d -> new DataIdentity(d.getDeptId(), d.getDeptName()))
                .collect(Collectors.toList());
    }

}
