package com.wzkris.gateway.service;

import com.wzkris.common.core.enums.AuthTypeEnum;
import com.wzkris.gateway.domain.ApiCallDailyStatBundle;
import com.wzkris.gateway.domain.ApiCallRealtimeStatBundle;

public interface ApiCallStatService {

    ApiCallDailyStatBundle queryDaily(AuthTypeEnum authType, String date);

    ApiCallRealtimeStatBundle queryRealtime(AuthTypeEnum authType, int windowSeconds);

}
