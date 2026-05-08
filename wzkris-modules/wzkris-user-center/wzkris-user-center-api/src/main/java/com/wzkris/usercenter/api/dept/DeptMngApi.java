package com.wzkris.usercenter.api.dept;

import com.wzkris.common.core.model.Result;
import com.wzkris.common.orm.request.IdRequest;
import com.wzkris.usercenter.request.dept.DeptMngListRequest;
import com.wzkris.usercenter.request.dept.DeptMngSaveRequest;
import com.wzkris.usercenter.request.dept.DeptMngUpdateRequest;
import com.wzkris.usercenter.response.dept.DeptMngResponse;

import java.util.List;

public interface DeptMngApi {

    Result<List<DeptMngResponse>> queryList(DeptMngListRequest request);

    Result<DeptMngResponse> queryInfo(IdRequest request);

    Result<?> save(DeptMngSaveRequest request);

    Result<?> update(DeptMngUpdateRequest request);

    Result<?> remove(IdRequest request);

}
