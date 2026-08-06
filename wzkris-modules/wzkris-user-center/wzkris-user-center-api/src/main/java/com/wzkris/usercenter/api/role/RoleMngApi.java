package com.wzkris.usercenter.api.role;

import com.wzkris.common.core.model.Result;
import com.wzkris.common.orm.model.Page;
import com.wzkris.common.orm.request.IdListRequest;
import com.wzkris.common.orm.request.IdRequest;
import com.wzkris.usercenter.api.role.request.RoleMngPageRequest;
import com.wzkris.usercenter.api.role.request.RoleMngSaveRequest;
import com.wzkris.usercenter.api.role.request.RoleMngUpdateRequest;
import com.wzkris.usercenter.api.role.response.RoleMngQueryResponse;
import com.wzkris.usercenter.api.role.response.RoleMngPageResponse;
import com.wzkris.usercenter.response.CheckedSelectResponse;
import com.wzkris.usercenter.response.CheckedSelectTreeResponse;

public interface RoleMngApi {

    Result<Page<RoleMngPageResponse>> queryPage(RoleMngPageRequest request);

    Result<RoleMngQueryResponse> queryInfo(IdRequest request);

    Result<CheckedSelectTreeResponse> queryMenuSelectTree(IdRequest request);

    Result<CheckedSelectTreeResponse> queryDeptSelectTree(IdRequest request);

    Result<CheckedSelectResponse> queryRoleInheritedSelect(IdRequest request);

    Result<Void> save(RoleMngSaveRequest request);

    Result<Void> update(RoleMngUpdateRequest request);

    Result<Void> remove(IdListRequest request);

}
