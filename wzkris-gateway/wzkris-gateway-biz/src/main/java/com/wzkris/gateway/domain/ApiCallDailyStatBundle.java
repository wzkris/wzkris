package com.wzkris.gateway.domain;

import lombok.Builder;

import java.util.Map;

@Builder
public record ApiCallDailyStatBundle(
        String date,
        ApiCallStatsDO total,
        Map<String, ApiCallStatsDO> hours,
        Map<String, ApiCallStatsDO> paths) {

}
