package com.wzkris.track.controller.track;

import com.wzkris.common.core.enums.BizBaseCodeEnum;
import com.wzkris.common.core.model.Result;
import com.wzkris.track.api.track.TrackIngestApi;
import com.wzkris.track.constants.TrackHttpHeaders;
import com.wzkris.track.request.track.TrackBatchIngestRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Tag(name = "埋点上报")
@Validated
@RestController
@RequestMapping("/track")
@RequiredArgsConstructor
public class TrackIngestController {

    private final TrackIngestApi trackIngestApi;

    @Operation(summary = "批量上报事件")
    @PostMapping("/events")
    public ResponseEntity<Result<Void>> events(
            @RequestHeader(value = TrackHttpHeaders.X_APP_KEY, required = false) String appKey,
            @RequestHeader(value = TrackHttpHeaders.X_APP_SECRET, required = false) String appSecret,
            @Valid @RequestBody TrackBatchIngestRequest body) {
        Result<Void> r = trackIngestApi.ingest(appKey, appSecret, body);
        if (r.isSuccess()) {
            return ResponseEntity.ok(r);
        }
        if (r.getCode() == BizBaseCodeEnum.AUTHENTICATION_ERROR.value()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(r);
        }
        if (r.getCode() == BizBaseCodeEnum.TOO_MANY_REQUESTS.value()) {
            return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS).body(r);
        }
        return ResponseEntity.badRequest().body(r);
    }

}
