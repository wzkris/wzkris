package com.wzkris.track.api.track;

import com.wzkris.common.core.model.Result;
import com.wzkris.track.api.track.request.TrackBatchIngestRequest;

public interface TrackIngestApi {

    Result<Void> ingest(String appKey, String appSecret, TrackBatchIngestRequest request);

}
