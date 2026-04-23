package com.wzkris.auth.controller;

import com.wzkris.auth.api.QrLoginApi;
import com.wzkris.auth.response.QrTokenResponse;
import com.wzkris.common.core.model.Result;
import com.wzkris.common.web.annotation.ExcludeLogAspect;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
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
    public Result<Void> scan(@Valid @RequestBody String qrcodeId) {
        return qrLoginApi.scan(qrcodeId);
    }

    @Operation(summary = "扫码确认")
    @PostMapping("/confirm")
    public Result<Void> confirm(@Valid @RequestBody String qrcodeId) {
        return qrLoginApi.confirm(qrcodeId);
    }

    @Operation(summary = "轮询获取扫码结果")
    @GetMapping("/poll-status")
    public Result<QrTokenResponse> pollstatus(
            @NotBlank(message = "{invalidParameter.param.invalid}")
            @RequestParam String qrcodeId
    ) {
        return qrLoginApi.pollstatus(qrcodeId);
    }

}
