package com.wzkris.common.remote.properties;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.context.properties.bind.Binder;
import org.springframework.boot.context.properties.source.MapConfigurationPropertySource;

import java.time.Duration;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DisplayName("RemoteInterfaceProperties 单测")
class RemoteInterfacePropertiesTest {

    @Test
    @DisplayName("connectTimeout和readTimeout应使用同一timeUnit换算")
    void testTimeoutDurationUsesConfiguredTimeUnit() {
        RemoteInterfaceProperties properties = new RemoteInterfaceProperties();
        properties.getHttp().setTimeUnit("SECONDS");
        properties.getHttp().setConnectTimeout(2);
        properties.getHttp().setReadTimeout(3);

        assertEquals(Duration.ofSeconds(2), properties.getHttp().getConnectTimeoutDuration());
        assertEquals(Duration.ofSeconds(3), properties.getHttp().getReadTimeoutDuration());
    }

    @Test
    @DisplayName("followRedirects应按配置解析")
    void testFollowRedirectsPolicyUsesConfiguredValue() {
        RemoteInterfaceProperties properties = new RemoteInterfaceProperties();
        properties.getHttp().setFollowRedirects("never");

        assertEquals(java.net.http.HttpClient.Redirect.NEVER, properties.getHttp().getFollowRedirectsPolicy());
    }

    @Test
    @DisplayName("应支持按http和observation分组绑定配置")
    void testGroupedPropertiesBinding() {
        Binder binder = new Binder(new MapConfigurationPropertySource(Map.of(
                "remote-interface.http.time-unit", "SECONDS",
                "remote-interface.http.connect-timeout", "2",
                "remote-interface.http.read-timeout", "3",
                "remote-interface.observation.max-body-length", "128"
        )));

        RemoteInterfaceProperties properties = binder.bind("remote-interface", RemoteInterfaceProperties.class)
                .orElseGet(RemoteInterfaceProperties::new);

        assertEquals(Duration.ofSeconds(2), properties.getHttp().getConnectTimeoutDuration());
        assertEquals(Duration.ofSeconds(3), properties.getHttp().getReadTimeoutDuration());
        assertEquals(128, properties.getObservation().getMaxBodyLength());
    }

}
