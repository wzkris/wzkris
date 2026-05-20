package com.wzkris.gateway.domain;

import lombok.Builder;

@Builder
public record ApiCallRealtimeStatBundle(int windowSeconds, ApiCallStatsDO stats) {

}
