package com.wzkris.usercenter.impl.role;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.wzkris.common.core.model.Result;
import com.wzkris.common.core.utils.StringUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.wzkris.common.web.model.AbstractApi;
import com.wzkris.common.orm.model.Page;
import com.wzkris.common.orm.request.IdListRequest;
import com.wzkris.common.orm.request.IdRequest;
import com.wzkris.common.security.utils.SecurityUtil;
import com.wzkris.common.core.utils.BeanCopierUtil;
import com.wzkris.usercenter.api.role.RoleMngApi;
import com.wzkris.usercenter.api.role.request.RoleMngPageRequest;
import com.wzkris.usercenter.api.role.request.RoleMngSaveRequest;
import com.wzkris.usercenter.api.role.request.RoleMngUpdateRequest;
import com.wzkris.usercenter.api.role.response.RoleMngResponse;
import com.wzkris.usercenter.domain.RoleInfoDO;
import com.wzkris.usercenter.mapper.RoleInfoMapper;
import com.wzkris.usercenter.mapper.RoleInheritanceMapper;
import com.wzkris.usercenter.mapper.RoleToDeptMapper;
import com.wzkris.usercenter.response.CheckedSelectResponse;
import com.wzkris.usercenter.response.CheckedSelectTreeResponse;
import com.wzkris.usercenter.response.SelectResponse;
import com.wzkris.usercenter.service.DeptInfoService;
import com.wzkris.usercenter.service.MenuInfoService;
import com.wzkris.usercenter.service.RoleInfoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class RoleMngApiImpl extends AbstractApi implements RoleMngApi {

    private final RoleInfoMapper roleInfoMapper;

    private final RoleInfoService roleInfoService;

    private final MenuInfoService menuInfoService;

    private final RoleToDeptMapper roleToDeptMapper;

    private final DeptInfoService deptInfoService;

    private final RoleInheritanceMapper roleInheritanceMapper;

    @Override
    public Result<Page<RoleMngResponse>> queryPage(RoleMngPageRequest request) {
        IPage<RoleInfoDO> page = roleInfoMapper.selectPageList(request.buildPage(), this.buildQueryWrapper(request));
        return ok(Page.of(page, BeanCopierUtil.copyList(page.getRecords(), RoleMngResponse.class)));
    }

    private LambdaQueryWrapper<RoleInfoDO> buildQueryWrapper(RoleMngPageRequest request) {
        return new LambdaQueryWrapper<RoleInfoDO>()
                .apply("r.deleted = false")
                .like(StringUtil.isNotEmpty(request.getRoleName()), RoleInfoDO::getRoleName, request.getRoleName())
                .eq(request.getStatus() != null, RoleInfoDO::getStatus, request.getStatus())
                .orderByDesc(RoleInfoDO::getRoleSort, RoleInfoDO::getRoleId);
    }

    @Override
    public Result<RoleMngResponse> queryInfo(IdRequest request) {
        Long roleId = request.getId();
        if (!roleInfoMapper.checkDataScopes(roleId)) {
            return accessDenied("数据权限不足");
        }
        return ok(BeanCopierUtil.copy(roleInfoService.getById(roleId), RoleMngResponse.class));
    }

    @Override
    public Result<CheckedSelectTreeResponse> queryMenuSelectTree(IdRequest request) {
        Long roleId = request.getId();
        if (!roleInfoMapper.checkDataScopes(roleId)) {
            return accessDenied("数据权限不足");
        }
        CheckedSelectTreeResponse checkedSelectTreeResponse = new CheckedSelectTreeResponse();
        checkedSelectTreeResponse.setCheckedKeys(menuInfoService.listMenuIdByRoleId(roleId));
        checkedSelectTreeResponse.setSelectTrees(menuInfoService.listSystemSelectTree(SecurityUtil.getUid()));
        return ok(checkedSelectTreeResponse);
    }

    @Override
    public Result<CheckedSelectTreeResponse> queryDeptSelectTree(IdRequest request) {
        Long roleId = request.getId();
        if (!roleInfoMapper.checkDataScopes(roleId)) {
            return accessDenied("数据权限不足");
        }
        CheckedSelectTreeResponse checkedSelectTreeResponse = new CheckedSelectTreeResponse();
        checkedSelectTreeResponse.setCheckedKeys(
                roleId == null ? Collections.emptyList() : roleToDeptMapper.listDeptIdByRoleIds(Collections.singletonList(roleId)));
        checkedSelectTreeResponse.setSelectTrees(deptInfoService.listSelectTree(null));
        return ok(checkedSelectTreeResponse);
    }

    @Override
    public Result<CheckedSelectResponse> queryRoleInheritedSelect(IdRequest request) {
        Long roleId = request.getId();
        if (!roleInfoMapper.checkDataScopes(roleId)) {
            return accessDenied("数据权限不足");
        }
        CheckedSelectResponse checkedSelectResponse = new CheckedSelectResponse();
        checkedSelectResponse.setCheckedKeys(roleId == null
                ? Collections.emptyList()
                : roleInheritanceMapper.listChildIdsByRoleId(roleId));
        List<SelectResponse> selectResponses = roleInfoService.listRoleSelect(null)
                .stream().filter(role -> !Objects.equals(role.getId(), roleId)).toList();
        checkedSelectResponse.setSelects(selectResponses);
        return ok(checkedSelectResponse);
    }

    @Override
    public Result<Void> save(RoleMngSaveRequest request) {
        RoleInfoDO role = BeanCopierUtil.copy(request, RoleInfoDO.class);
        role.setStatus(request.getStatus());
        return toRes(roleInfoService.saveRole(role, request.getMenuIds(), request.getDeptIds(), request.getChildIds()));
    }

    @Override
    public Result<Void> update(RoleMngUpdateRequest request) {
        if (request.getChildIds() != null && request.getChildIds().contains(request.getRoleId())) {
            return requestFail("角色不能继承自身");
        }
        if (request.getChildIds() != null && roleInheritanceMapper
                .listChildIdsRecursive(request.getChildIds())
                .contains(request.getRoleId())) {
            return requestFail("角色继承关系存在循环");
        }
        RoleInfoDO role = BeanCopierUtil.copy(request, RoleInfoDO.class);
        role.setStatus(request.getStatus());
        return toRes(roleInfoService.updateRole(role, request.getMenuIds(), request.getDeptIds(), request.getChildIds()));
    }

    @Override
    public Result<Void> remove(IdListRequest request) {
        List<Long> roleIds = request.getIdList();
        if (!roleInfoMapper.checkDataScopes(roleIds)) {
            return accessDenied("数据权限不足");
        }
        if (roleInfoService.existAdmin(roleIds)) {
            return requestFail("当前角色已被分配用户");
        }
        if (roleInfoService.existChildRole(roleIds)) {
            return requestFail("当前角色已被其他角色继承");
        }
        return toRes(roleInfoService.removeRoles(roleIds));
    }

}
