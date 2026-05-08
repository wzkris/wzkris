package com.wzkris.track.props;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.HashMap;
import java.util.Map;

@Data
@ConfigurationProperties(prefix = "wzkris.track")
public class TrackProperties {

    private Map<String, String> apps = new HashMap<>();

    private int maxEventsPerBatch = 500;

    private RateLimit rateLimit = new RateLimit();

    public boolean matchesCredentials(String appKey, String appSecret) {
        if (appKey == null || appSecret == null) {
            return false;
        }
        String expected = apps.get(appKey);
        if (expected == null) {
            return false;
        }
        return MessageDigest.isEqual(
                expected.getBytes(StandardCharsets.UTF_8), appSecret.getBytes(StandardCharsets.UTF_8));
    }

    @Data
    public static class RateLimit {

        private int requestsPerMinute = 3000;

    }

}
