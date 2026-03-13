package com.wzkris.mqtt.model;

public final class SessionRegistration {

    private final MqttSession currentSession;

    private final MqttSession previousSession;

    public SessionRegistration(MqttSession currentSession, MqttSession previousSession) {
        this.currentSession = currentSession;
        this.previousSession = previousSession;
    }

    public MqttSession getCurrentSession() {
        return currentSession;
    }

    public MqttSession getPreviousSession() {
        return previousSession;
    }

}
