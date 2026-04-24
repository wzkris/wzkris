package com.wzkris.auth.controller;

import com.wzkris.auth.api.QrLoginApi;
import com.wzkris.auth.request.QrCodeIdRequest;
import com.wzkris.auth.response.QrTokenResponse;
import com.wzkris.common.core.model.Result;
import com.wzkris.common.web.annotation.ExcludeLogAspect;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Tag(name = "二维码登录")
@Slf4j
@Validated
@ExcludeLogAspect
@RestController
@RequestMapping("/qr-code")
@RequiredArgsConstructor
public class QrLoginController {

    private final QrLoginApi qrLoginApi;

    @Operation(summary = "二维码")
    @GetMapping
    public Result<?> qrcode() {
        return qrLoginApi.qrcode();
    }

    @Operation(summary = "扫码")
    @PostMapping("/scan")
    public Result<Void> scan(@Valid @RequestBody QrCodeIdRequest request) {
        return qrLoginApi.scan(request);
    }

    @Operation(summary = "扫码确认")
    @PostMapping("/confirm")
    public Result<Void> confirm(@Valid @RequestBody QrCodeIdRequest request) {
        return qrLoginApi.confirm(request);
    }

    @Operation(summary = "轮询获取扫码结果")
    @GetMapping("/poll-status")
    public Result<QrTokenResponse> pollstatus(@Valid QrCodeIdRequest request) {
        return qrLoginApi.pollstatus(request);
    }

}
