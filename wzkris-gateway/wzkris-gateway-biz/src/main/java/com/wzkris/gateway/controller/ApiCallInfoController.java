package com.wzkris.gateway.controller;

import com.wzkris.common.core.model.Result;
import com.wzkris.common.security.annotation.CheckAdminPerms;
import com.wzkris.gateway.api.apicall.ApiCallInfoApi;
import com.wzkris.gateway.api.apicall.request.ApiCallDailyStatRequest;
import com.wzkris.gateway.api.apicall.request.ApiCallRealtimeStatRequest;
import com.wzkris.gateway.api.apicall.response.ApiCallDailySeriesResponse;
import com.wzkris.gateway.api.apicall.response.ApiCallRealtimeResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api-call-info")
@RequiredArgsConstructor
@CheckAdminPerms("gateway-mod:statistics:pvuv")
public class ApiCallInfoController {

    private final ApiCallInfoApi apiCallInfoApi;

    @GetMapping("/query-daily")
    public Result<ApiCallDailySeriesResponse> queryDaily(ApiCallDailyStatRequest request) {
        return apiCallInfoApi.queryDaily(request);
    }

    @GetMapping("/query-realtime")
    public Result<ApiCallRealtimeResponse> queryRealtime(ApiCallRealtimeStatRequest request) {
        return apiCallInfoApi.queryRealtime(request);
    }

}
