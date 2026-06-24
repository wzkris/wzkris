package com.wzkris.usercenter.api.dept;

import com.wzkris.common.core.model.Result;
import com.wzkris.common.orm.request.IdRequest;
import com.wzkris.usercenter.api.dept.request.DeptMngTreeRequest;
import com.wzkris.usercenter.api.dept.request.DeptMngSaveRequest;
import com.wzkris.usercenter.api.dept.request.DeptMngUpdateRequest;
import com.wzkris.usercenter.api.dept.response.DeptMngResponse;

import java.util.List;

public interface DeptMngApi {

    Result<List<DeptMngResponse>> queryList(DeptMngTreeRequest request);

    Result<DeptMngResponse> queryInfo(IdRequest request);

    Result<?> save(DeptMngSaveRequest request);

    Result<?> update(DeptMngUpdateRequest request);

    Result<?> remove(IdRequest request);

}
