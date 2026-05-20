package com.wzkris.gateway.api.apicall;

import com.wzkris.common.core.model.Result;
import com.wzkris.gateway.api.apicall.request.ApiCallDailyQueryRequest;
import com.wzkris.gateway.api.apicall.request.ApiCallRealtimeQueryRequest;
import com.wzkris.gateway.api.apicall.response.ApiCallDailySeriesResponse;
import com.wzkris.gateway.api.apicall.response.ApiCallRealtimeResponse;

public interface ApiCallInfoApi {

    Result<ApiCallDailySeriesResponse> queryDaily(ApiCallDailyQueryRequest request);

    Result<ApiCallRealtimeResponse> queryRealtime(ApiCallRealtimeQueryRequest request);

}
