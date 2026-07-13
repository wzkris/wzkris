package com.wzkris.usercenter.remote.api.operatelog;

import com.wzkris.common.core.model.Result;
import com.wzkris.usercenter.remote.api.operatelog.request.OperateLogEventRequest;

import java.util.List;

public interface OperateLogRemoteApi {

    Result<Void> save(List<OperateLogEventRequest> requestList);

}

