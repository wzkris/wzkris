package com.wzkris.usercenter.impl.tenantrole;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.wzkris.common.core.model.Result;
import com.wzkris.common.core.utils.BeanCopierUtil;
import com.wzkris.common.core.utils.StringUtil;
import com.wzkris.common.orm.model.Page;
import com.wzkris.common.orm.request.IdListRequest;
import com.wzkris.common.orm.request.IdRequest;
import com.wzkris.common.security.utils.SecurityUtil;
import com.wzkris.common.web.model.AbstractApi;
import com.wzkris.usercenter.api.tenantrole.TenantRoleMngApi;
import com.wzkris.usercenter.api.tenantrole.request.TenantRoleMngPageRequest;
import com.wzkris.usercenter.api.tenantrole.request.TenantRoleMngSaveRequest;
import com.wzkris.usercenter.api.tenantrole.request.TenantRoleMngUpdateRequest;
import com.wzkris.usercenter.api.tenantrole.response.TenantRoleMngPageResponse;
import com.wzkris.usercenter.api.tenantrole.response.TenantRoleMngQueryResponse;
import com.wzkris.usercenter.domain.TenantRoleDO;
import com.wzkris.usercenter.response.CheckedSelectTreeResponse;
import com.wzkris.usercenter.service.MenuInfoService;
import com.wzkris.usercenter.service.TenantRoleService;
import com.wzkris.usercenter.service.TenantInfoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TenantRoleMngApiImpl extends AbstractApi implements TenantRoleMngApi {

    private final TenantInfoService tenantInfoService;

    private final TenantRoleService tenantRoleService;

    private final MenuInfoService menuInfoService;

    @Override
    public Result<Page<TenantRoleMngPageResponse>> queryPage(TenantRoleMngPageRequest request) {
        IPage<TenantRoleDO> page = tenantRoleService.page(request.buildPage(), this.buildQueryWrapper(request));
        return ok(Page.of(page, BeanCopierUtil.copyList(page.getRecords(), TenantRoleMngPageResponse.class)));
    }

    private LambdaQueryWrapper<TenantRoleDO> buildQueryWrapper(TenantRoleMngPageRequest request) {
        return new LambdaQueryWrapper<TenantRoleDO>()
                .like(StringUtil.isNotEmpty(request.getRoleName()), TenantRoleDO::getRoleName, request.getRoleName())
                .eq(request.getStatus() != null, TenantRoleDO::getStatus, request.getStatus())
                .orderByDesc(TenantRoleDO::getRoleSort, TenantRoleDO::getId);
    }

    @Override
    public Result<TenantRoleMngQueryResponse> queryById(IdRequest request) {
        return ok(BeanCopierUtil.copy(tenantRoleService.getById(request.getId()), TenantRoleMngQueryResponse.class));
    }

    @Override
    public Result<CheckedSelectTreeResponse> queryMenuSelectTree(IdRequest request) {
        Long tenantRoleId = request.getId();
        CheckedSelectTreeResponse checkedSelectTreeResponse = new CheckedSelectTreeResponse();
        checkedSelectTreeResponse.setCheckedKeys(menuInfoService.listMenuIdByTenantRoleId(tenantRoleId));
        checkedSelectTreeResponse.setSelectTrees(menuInfoService.listTenantSelectTree(SecurityUtil.getUid()));
        return ok(checkedSelectTreeResponse);
    }

    @Override
    public Result<Void> save(TenantRoleMngSaveRequest request) {
        if (!tenantInfoService.checkRoleLimit(SecurityUtil.getTenantId())) {
            return requestFail("当前租户角色数量已达到上限");
        }
        TenantRoleDO role = BeanCopierUtil.copy(request, TenantRoleDO.class);
        role.setStatus(request.getStatus());
        return toRes(tenantRoleService.saveRole(role, request.getMenuIds()));
    }

    @Override
    public Result<Void> update(TenantRoleMngUpdateRequest request) {
        TenantRoleDO role = BeanCopierUtil.copy(request, TenantRoleDO.class);
        role.setStatus(request.getStatus());
        return toRes(tenantRoleService.updateRole(role, request.getMenuIds()));
    }

    @Override
    public Result<Void> remove(IdListRequest request) {
        List<Long> tenantRoleIds = request.getIdList();
        if (tenantRoleService.existTenantUser(tenantRoleIds)) {
            return requestFail("当前角色已被分配");
        }
        return toRes(tenantRoleService.removeRoles(tenantRoleIds));
    }

}
