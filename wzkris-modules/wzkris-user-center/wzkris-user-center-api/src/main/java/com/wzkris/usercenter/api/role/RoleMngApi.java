package com.wzkris.usercenter.api.role;

import com.wzkris.common.core.model.Result;
import com.wzkris.common.orm.model.Page;
import com.wzkris.usercenter.request.StatusUpdateRequest;
import com.wzkris.usercenter.request.role.RoleMngQueryRequest;
import com.wzkris.usercenter.request.role.RoleMngSaveRequest;
import com.wzkris.usercenter.request.role.RoleMngUpdateRequest;
import com.wzkris.usercenter.response.CheckedSelectResponse;
import com.wzkris.usercenter.response.CheckedSelectTreeResponse;
import com.wzkris.usercenter.response.role.RoleInfoResponse;

import java.util.List;

public interface RoleMngApi {

    Result<Page<RoleInfoResponse>> queryPage(RoleMngQueryRequest request);

    Result<RoleInfoResponse> queryInfo(Long roleId);

    Result<CheckedSelectTreeResponse> queryRoleMenuSelectTree(Long roleId);

    Result<CheckedSelectTreeResponse> queryRoleDeptSelectTree(Long roleId);

    Result<CheckedSelectResponse> queryRoleInheritedSelect(Long roleId);

    Result<Void> save(RoleMngSaveRequest request);

    Result<Void> update(RoleMngUpdateRequest request);

    Result<Void> updateStatus(StatusUpdateRequest request);

    Result<Void> remove(List<Long> roleIds);

}
