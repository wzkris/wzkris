package com.wzkris.track.config;

import com.wzkris.track.props.TrackProperties;
import com.wzkris.track.ratelimit.AppKeyRateLimiter;
import com.wzkris.track.sink.LoggingTrackEventSink;
import com.wzkris.track.sink.TrackEventSink;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(TrackProperties.class)
public class TrackConfiguration {

    @Bean
    @ConditionalOnMissingBean
    TrackEventSink loggingTrackEventSink() {
        return new LoggingTrackEventSink();
    }

    @Bean
    AppKeyRateLimiter appKeyRateLimiter(TrackProperties trackProperties) {
        int rpm = trackProperties.getRateLimit().getRequestsPerMinute();
        return new AppKeyRateLimiter(rpm, 60_000L);
    }

}
