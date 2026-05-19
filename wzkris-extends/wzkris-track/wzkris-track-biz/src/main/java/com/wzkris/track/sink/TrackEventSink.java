package com.wzkris.track.sink;

import com.wzkris.track.api.track.request.TrackEventRequest;

import java.util.List;

public interface TrackEventSink {

    void accept(String appKey, List<TrackEventRequest> events);

}
