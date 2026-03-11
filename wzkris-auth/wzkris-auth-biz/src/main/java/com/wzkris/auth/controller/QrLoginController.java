package com.wzkris.auth.controller;

import com.wzkris.auth.constants.QrCodeConstant;
import com.wzkris.auth.domain.vo.QrTokenVO;
import com.wzkris.auth.enums.QrCodeStatusEnum;
import com.wzkris.auth.service.TokenService;
import com.wzkris.common.core.model.BaseLoginUser;
import com.wzkris.common.core.model.Result;
import com.wzkris.common.core.utils.StringUtil;
import com.wzkris.common.security.utils.SecurityUtil;
import com.wzkris.common.web.annotation.ExControllerStat;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.Duration;
import java.util.*;

@Tag(name = "二维码登录")
@Slf4j
@Validated
@ExControllerStat
@RestController
@RequestMapping("/qr-code")
@RequiredArgsConstructor
public class QrLoginController {

    private final TokenService tokenService;

    private final RedisTemplate<String, Object> redisTemplate;

    @Operation(summary = "二维码")
    @GetMapping
    public Result<?> qrcode() {
        String qrcodeId = UUID.randomUUID().toString();
        //定义二维码参数
        Map<String, String> params = new HashMap<>(2);
        params.put("qrcodeId", qrcodeId);
        //存放二维码唯一标识30秒有效
        redisTemplate.opsForValue().set(QrCodeConstant.LOGIN_QRCODE_CACHE + qrcodeId,
                new QrTokenVO(QrCodeStatusEnum.WAIT.getValue(), null, null), Duration.ofSeconds(60));
        return Result.ok(params);
    }

    @Operation(summary = "扫码")
    @PostMapping("/scan")
    public Result<Void> scan(@Valid @RequestBody String qrcodeId) {
        String key = QrCodeConstant.LOGIN_QRCODE_CACHE + qrcodeId;
        Object value = redisTemplate.opsForValue().get(key);
        QrTokenVO qrTokenVO = value instanceof QrTokenVO ? (QrTokenVO) value : null;
        if (Objects.isNull(qrTokenVO)) {
            return Result.requestFail("二维码已过期");
        }
        if (!StringUtil.equals(qrTokenVO.getStatus(), QrCodeStatusEnum.WAIT.getValue())) {
            return Result.requestFail("二维码已被扫描");
        }
        qrTokenVO.setStatus(QrCodeStatusEnum.SCANED.getValue());
        redisTemplate.opsForValue().set(key, qrTokenVO, Duration.ofSeconds(60));
        return Result.ok();
    }

    @Operation(summary = "扫码确认")
    @PostMapping("/confirm")
    public Result<Void> confirm(@Valid @RequestBody String qrcodeId) {
        String key = QrCodeConstant.LOGIN_QRCODE_CACHE + qrcodeId;
        Object value = redisTemplate.opsForValue().get(key);
        QrTokenVO qrTokenVO = value instanceof QrTokenVO ? (QrTokenVO) value : null;
        if (Objects.isNull(qrTokenVO)) {
            return Result.requestFail("二维码已过期");
        }
        if (!StringUtil.equals(qrTokenVO.getStatus(), QrCodeStatusEnum.SCANED.getValue())) {
            return Result.requestFail("二维码已被扫描");
        }

        BaseLoginUser loginUser = SecurityUtil.getLoginUser();
        Set<String> permission = SecurityUtil.getPermission();
        // 生成新的sid
        String sid = UUID.randomUUID().toString();
        String accessToken = tokenService.generateAccessToken(loginUser, sid);
        String refreshToken = tokenService.generateRefreshToken(loginUser, sid);
        tokenService.save(loginUser, sid, permission);

        qrTokenVO.setStatus(QrCodeStatusEnum.CONFIRM.getValue());
        qrTokenVO.setAccessToken(accessToken);
        qrTokenVO.setRefreshToken(refreshToken);
        redisTemplate.opsForValue().set(key, qrTokenVO, Duration.ofSeconds(60));
        return Result.ok();
    }

    @Operation(summary = "轮询获取扫码结果")
    @GetMapping("/poll-status")
    public Result<QrTokenVO> pollstatus(
            @NotBlank(message = "{invalidParameter.param.invalid}")
            @RequestParam String qrcodeId
    ) {
        Object value = redisTemplate.opsForValue().get(QrCodeConstant.LOGIN_QRCODE_CACHE + qrcodeId);
        QrTokenVO qrTokenVO = value instanceof QrTokenVO ? (QrTokenVO) value : null;
        if (Objects.isNull(qrTokenVO)) {
            return Result.ok(QrTokenVO.OVERDUE());
        }
        return Result.ok(qrTokenVO);
    }

}
