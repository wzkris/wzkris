package com.wzkris.track.service;

import com.wzkris.common.core.enums.BizBaseCodeEnum;
import com.wzkris.common.core.model.Result;
import com.wzkris.track.props.TrackProperties;
import com.wzkris.track.ratelimit.AppKeyRateLimiter;
import com.wzkris.track.request.track.TrackEventRequest;
import com.wzkris.track.sink.TrackEventSink;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TrackIngestService {

    private final TrackProperties trackProperties;

    private final AppKeyRateLimiter appKeyRateLimiter;

    private final TrackEventSink trackEventSink;

    public Result<Void> ingest(String appKey, String appSecret, List<TrackEventRequest> events) {
        if (!trackProperties.matchesCredentials(appKey, appSecret)) {
            return Result.unauth("invalid app credentials");
        }
        if (!appKeyRateLimiter.tryAcquire(appKey)) {
            return Result.init(
                    BizBaseCodeEnum.TOO_MANY_REQUESTS.value(), null, BizBaseCodeEnum.TOO_MANY_REQUESTS.desc());
        }
        if (events.size() > trackProperties.getMaxEventsPerBatch()) {
            return Result.requestFail("events exceed maxEventsPerBatch");
        }
        long now = System.currentTimeMillis();
        for (TrackEventRequest e : events) {
            e.setServerReceivedAt(now);
        }
        trackEventSink.accept(appKey, events);
        return Result.ok();
    }

}
