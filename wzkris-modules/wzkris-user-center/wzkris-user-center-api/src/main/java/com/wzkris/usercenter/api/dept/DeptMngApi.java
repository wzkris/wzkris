package com.wzkris.usercenter.api.dept;

import com.wzkris.common.core.model.Result;
import com.wzkris.usercenter.request.common.IdRequest;
import com.wzkris.usercenter.request.dept.DeptMngListRequest;
import com.wzkris.usercenter.request.dept.DeptMngSaveRequest;
import com.wzkris.usercenter.request.dept.DeptMngUpdateRequest;
import com.wzkris.usercenter.response.dept.DeptInfoResponse;

import java.util.List;

public interface DeptMngApi {

    Result<List<DeptInfoResponse>> queryList(DeptMngListRequest request);

    Result<DeptInfoResponse> queryInfo(IdRequest request);

    Result<?> save(DeptMngSaveRequest request);

    Result<?> update(DeptMngUpdateRequest request);

    Result<?> remove(IdRequest request);

}
