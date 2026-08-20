package com.wzkris.gateway.api.apicall;

import com.wzkris.common.core.model.Result;
import com.wzkris.gateway.api.apicall.request.ApiCallDailyStatRequest;
import com.wzkris.gateway.api.apicall.request.ApiCallRealtimeStatRequest;
import com.wzkris.gateway.api.apicall.response.ApiCallDailySeriesResponse;
import com.wzkris.gateway.api.apicall.response.ApiCallRealtimeResponse;

public interface ApiCallInfoApi {

    Result<ApiCallDailySeriesResponse> queryDaily(ApiCallDailyStatRequest request);

    Result<ApiCallRealtimeResponse> queryRealtime(ApiCallRealtimeStatRequest request);

}
