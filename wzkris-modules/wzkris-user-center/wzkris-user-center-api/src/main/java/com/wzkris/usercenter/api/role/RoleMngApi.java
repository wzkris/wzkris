package com.wzkris.usercenter.api.role;

import com.wzkris.common.core.model.Result;
import com.wzkris.common.orm.model.Page;
import com.wzkris.common.orm.request.IdListRequest;
import com.wzkris.common.orm.request.IdRequest;
import com.wzkris.usercenter.request.role.RoleMngPageRequest;
import com.wzkris.usercenter.request.role.RoleMngSaveRequest;
import com.wzkris.usercenter.request.role.RoleMngUpdateRequest;
import com.wzkris.usercenter.response.common.CheckedSelectResponse;
import com.wzkris.usercenter.response.common.CheckedSelectTreeResponse;
import com.wzkris.usercenter.response.role.RoleMngResponse;

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
