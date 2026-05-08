package com.wzkris.track.impl.track;

import com.wzkris.common.core.model.Result;
import com.wzkris.track.api.track.TrackIngestApi;
import com.wzkris.track.request.track.TrackBatchIngestRequest;
import com.wzkris.track.service.TrackIngestService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TrackIngestApiImpl implements TrackIngestApi {

    private final TrackIngestService trackIngestService;

    @Override
    public Result<Void> ingest(String appKey, String appSecret, TrackBatchIngestRequest request) {
        return trackIngestService.ingest(appKey, appSecret, request.getEvents());
    }

}
