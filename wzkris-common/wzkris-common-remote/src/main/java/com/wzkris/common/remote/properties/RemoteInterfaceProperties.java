package com.wzkris.common.remote.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;
import org.springframework.cloud.context.config.annotation.RefreshScope;

import java.time.Duration;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

/**
 * @author : wzkris
 * @version : V1.0.0
 * @description : remote interface参数
 * @date : 2025/06/10 15:00
 */
@Data
@RefreshScope
@ConfigurationProperties(prefix = "remote-interface")
public class RemoteInterfaceProperties {

    @NestedConfigurationProperty
    private HttpProperties http = new HttpProperties();

    @NestedConfigurationProperty
    private ObservationProperties observation = new ObservationProperties();

    @Data
    public static class HttpProperties {

        private String timeUnit = "MILLISECONDS";

        private int connectTimeout = 10_000;

        private int readTimeout = 10_000;

        private String followRedirects = "NORMAL";

        private String executorThreadNamePrefix = "wzkris-http-client-";

        public Duration getConnectTimeoutDuration() {
            return Duration.of(connectTimeout, resolveTimeUnit().toChronoUnit());
        }

        public Duration getReadTimeoutDuration() {
            return Duration.of(readTimeout, resolveTimeUnit().toChronoUnit());
        }

        public java.net.http.HttpClient.Redirect getFollowRedirectsPolicy() {
            String configuredPolicy = followRedirects == null ? "NORMAL" : followRedirects;
            return java.net.http.HttpClient.Redirect.valueOf(configuredPolicy.toUpperCase(Locale.ROOT));
        }

        private TimeUnit resolveTimeUnit() {
            String configuredTimeUnit = timeUnit == null ? "MILLISECONDS" : timeUnit;
            return TimeUnit.valueOf(configuredTimeUnit.toUpperCase(Locale.ROOT));
        }

    }

    @Data
    public static class ObservationProperties {

        private boolean logEnabled = true;

        private int maxBodyLength = -1;

    }

}
