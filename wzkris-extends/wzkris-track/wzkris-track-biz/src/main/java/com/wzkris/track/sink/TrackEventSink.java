package com.wzkris.track.sink;

import com.wzkris.track.request.track.TrackEventRequest;

import java.util.List;

public interface TrackEventSink {

    void accept(String appKey, List<TrackEventRequest> events);

}
