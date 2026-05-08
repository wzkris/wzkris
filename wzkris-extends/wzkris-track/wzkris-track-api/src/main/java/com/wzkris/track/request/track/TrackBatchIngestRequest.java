package com.wzkris.track.request.track;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

import java.util.List;

@Data
@Schema(description = "批量埋点上报")
public class TrackBatchIngestRequest {

    @NotEmpty
    @Schema(description = "事件列表")
    private List<@Valid TrackEventRequest> events;

}
