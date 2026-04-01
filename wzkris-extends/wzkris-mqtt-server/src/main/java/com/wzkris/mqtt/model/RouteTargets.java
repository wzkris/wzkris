package com.wzkris.mqtt.model;

import lombok.Getter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Getter
public final class RouteTargets {

    private final List<MqttSession> normalTargets;

    private final MqttSession sharedTarget;

    public RouteTargets(List<MqttSession> normalTargets, MqttSession sharedTarget) {
        List<MqttSession> safeTargets = normalTargets == null ? Collections.emptyList() : new ArrayList<>(normalTargets);
        this.normalTargets = Collections.unmodifiableList(safeTargets);
        this.sharedTarget = sharedTarget;
    }

}
