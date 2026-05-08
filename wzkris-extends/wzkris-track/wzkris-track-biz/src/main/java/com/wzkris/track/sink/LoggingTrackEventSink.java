package com.wzkris.track.sink;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.wzkris.common.core.utils.JsonUtil;
import com.wzkris.track.request.track.TrackEventRequest;

import java.util.List;

public class LoggingTrackEventSink implements TrackEventSink {

    private static final org.slf4j.Logger LINE = org.slf4j.LoggerFactory.getLogger("track.event");

    @Override
    public void accept(String appKey, List<TrackEventRequest> events) {
        for (TrackEventRequest e : events) {
            ObjectNode node = JsonUtil.getObjectMapper().valueToTree(e);
            node.put("appKey", appKey);
            LINE.info(node.toString());
        }
    }

}
