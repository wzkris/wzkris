package com.wzkris.auth.controller;

import com.wzkris.auth.constants.QrCodeConstant;
import com.wzkris.auth.domain.resp.QrTokenResp;
import com.wzkris.auth.enums.QrCodeStatusEnum;
import com.wzkris.auth.service.TokenService;
import com.wzkris.common.core.model.MyPrincipal;
import com.wzkris.common.core.model.Result;
import com.wzkris.common.core.utils.StringUtil;
import com.wzkris.common.redis.util.RedisUtil;
import com.wzkris.common.security.utils.SecurityUtil;
import com.wzkris.common.web.annotation.ExControllerStat;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

@Tag(name = "二维码登录")
@Slf4j
@Validated
@ExControllerStat
@RestController
@RequestMapping("/qr-code")
@RequiredArgsConstructor
public class QrLoginController {

    private final TokenService tokenService;

    @Operation(summary = "二维码")
    @GetMapping
    public Result<?> qrcode() {
        String qrcodeId = UUID.randomUUID().toString();
        //定义二维码参数
        Map<String, String> params = new HashMap<>(2);
        params.put("qrcodeId", qrcodeId);
        //存放二维码唯一标识30秒有效
        RedisUtil.setObj(QrCodeConstant.LOGIN_QRCODE_CACHE + qrcodeId,
                new QrTokenResp(QrCodeStatusEnum.WAIT.getValue(), null, null), 60);
        return Result.ok(params);
    }

    @Operation(summary = "扫码")
    @PostMapping("/scan")
    public Result<Void> scan(@Valid @RequestBody String qrcodeId) {
        String key = QrCodeConstant.LOGIN_QRCODE_CACHE + qrcodeId;
        QrTokenResp qrTokenResp = RedisUtil.getObj(key, QrTokenResp.class);
        if (Objects.isNull(qrTokenResp)) {
            return Result.requestFail("二维码已过期");
        }
        if (!StringUtil.equals(qrTokenResp.getStatus(), QrCodeStatusEnum.WAIT.getValue())) {
            return Result.requestFail("二维码已被扫描");
        }
        qrTokenResp.setStatus(QrCodeStatusEnum.SCANED.getValue());
        RedisUtil.setObj(key, qrTokenResp, 60);
        return Result.ok();
    }

    @Operation(summary = "扫码确认")
    @PostMapping("/confirm")
    public Result<Void> confirm(@Valid @RequestBody String qrcodeId) {
        String key = QrCodeConstant.LOGIN_QRCODE_CACHE + qrcodeId;
        QrTokenResp qrTokenResp = RedisUtil.getObj(key, QrTokenResp.class);
        if (Objects.isNull(qrTokenResp)) {
            return Result.requestFail("二维码已过期");
        }
        if (!StringUtil.equals(qrTokenResp.getStatus(), QrCodeStatusEnum.SCANED.getValue())) {
            return Result.requestFail("二维码已被扫描");
        }

        MyPrincipal principal = SecurityUtil.getPrincipal();
        String accessToken = tokenService.generateAccessToken(principal);
        String refreshToken = tokenService.generateToken();
        tokenService.save(principal, accessToken, refreshToken);

        qrTokenResp.setStatus(QrCodeStatusEnum.CONFIRM.getValue());
        qrTokenResp.setAccessToken(accessToken);
        qrTokenResp.setRefreshToken(refreshToken);
        RedisUtil.setObj(key, qrTokenResp, 60);
        return Result.ok();
    }

    @Operation(summary = "轮询获取扫码结果")
    @GetMapping("/poll-status")
    public Result<QrTokenResp> pollstatus(
            @NotBlank(message = "{invalidParameter.param.invalid}")
            @RequestParam String qrcodeId
    ) {
        QrTokenResp qrTokenResp = RedisUtil.getObj(QrCodeConstant.LOGIN_QRCODE_CACHE + qrcodeId, QrTokenResp.class);
        if (Objects.isNull(qrTokenResp)) {
            return Result.ok(QrTokenResp.OVERDUE());
        }
        return Result.ok(qrTokenResp);
    }

}
