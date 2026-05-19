package com.wzkris.usercenter.api.role;

import com.wzkris.common.core.model.Result;
import com.wzkris.common.orm.model.Page;
import com.wzkris.common.orm.request.IdListRequest;
import com.wzkris.common.orm.request.IdRequest;
import com.wzkris.usercenter.api.role.request.RoleMngPageRequest;
import com.wzkris.usercenter.api.role.request.RoleMngSaveRequest;
import com.wzkris.usercenter.api.role.request.RoleMngUpdateRequest;
import com.wzkris.usercenter.api.role.response.RoleMngResponse;
import com.wzkris.usercenter.response.CheckedSelectResponse;
import com.wzkris.usercenter.response.CheckedSelectTreeResponse;

public interface RoleMngApi {

    Result<Page<RoleMngResponse>> queryPage(RoleMngPageRequest request);

    Result<RoleMngResponse> queryInfo(IdRequest request);

    Result<CheckedSelectTreeResponse> queryRoleMenuSelectTree(IdRequest request);

    Result<CheckedSelectTreeResponse> queryRoleDeptSelectTree(IdRequest request);

    Result<CheckedSelectResponse> queryRoleInheritedSelect(IdRequest request);

    Result<Void> save(RoleMngSaveRequest request);

    Result<Void> update(RoleMngUpdateRequest request);

    Result<Void> remove(IdListRequest request);

}
