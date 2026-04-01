package com.wzkris.mqtt.model;

import lombok.Getter;

@Getter
public final class RouteResult {

    private final int normalTargetCount;

    private final boolean sharedTargetMatched;

    private final int deliveredCount;

    public RouteResult(int normalTargetCount, boolean sharedTargetMatched, int deliveredCount) {
        this.normalTargetCount = normalTargetCount;
        this.sharedTargetMatched = sharedTargetMatched;
        this.deliveredCount = deliveredCount;
    }

}
