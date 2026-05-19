package com.wzkris.auth.impl.token;

import com.wzkris.auth.api.token.QrLoginApi;
import com.wzkris.auth.api.token.request.QrCodeIdRequest;
import com.wzkris.auth.api.token.response.QrTokenResponse;
import com.wzkris.auth.constants.QrCodeConstant;
import com.wzkris.auth.domain.TokenPair;
import com.wzkris.auth.enums.QrCodeStatusEnum;
import com.wzkris.auth.service.TokenService;
import com.wzkris.common.core.model.BaseLoginUser;
import com.wzkris.common.core.model.Result;
import com.wzkris.common.core.utils.StringUtil;
import com.wzkris.common.security.utils.SecurityUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.*;

@Service
@RequiredArgsConstructor
public class QrLoginApiImpl implements QrLoginApi {

    private final TokenService tokenService;

    private final RedisTemplate<String, Object> redisTemplate;

    @Override
    public Result<?> qrcode() {
        String qrcodeId = UUID.randomUUID().toString();
        Map<String, String> params = new HashMap<>(2);
        params.put("qrcodeId", qrcodeId);
        redisTemplate.opsForValue().set(QrCodeConstant.LOGIN_QRCODE_CACHE + qrcodeId,
                new QrTokenResponse(QrCodeStatusEnum.WAIT.getValue(), null, null), Duration.ofSeconds(60));
        return Result.ok(params);
    }

    @Override
    public Result<Void> scan(QrCodeIdRequest request) {
        String qrcodeId = request.getQrcodeId();
        String key = QrCodeConstant.LOGIN_QRCODE_CACHE + qrcodeId;
        Object value = redisTemplate.opsForValue().get(key);
        QrTokenResponse qrTokenResponse = value instanceof QrTokenResponse ? (QrTokenResponse) value : null;
        if (Objects.isNull(qrTokenResponse)) {
            return Result.requestFail("二维码已过期");
        }
        if (!StringUtil.equals(qrTokenResponse.getStatus(), QrCodeStatusEnum.WAIT.getValue())) {
            return Result.requestFail("二维码已被扫描");
        }
        qrTokenResponse.setStatus(QrCodeStatusEnum.SCANED.getValue());
        redisTemplate.opsForValue().set(key, qrTokenResponse, Duration.ofSeconds(60));
        return Result.ok();
    }

    @Override
    public Result<Void> confirm(QrCodeIdRequest request) {
        String qrcodeId = request.getQrcodeId();
        String key = QrCodeConstant.LOGIN_QRCODE_CACHE + qrcodeId;
        Object value = redisTemplate.opsForValue().get(key);
        QrTokenResponse qrTokenResponse = value instanceof QrTokenResponse ? (QrTokenResponse) value : null;
        if (Objects.isNull(qrTokenResponse)) {
            return Result.requestFail("二维码已过期");
        }
        if (!StringUtil.equals(qrTokenResponse.getStatus(), QrCodeStatusEnum.SCANED.getValue())) {
            return Result.requestFail("二维码已被扫描");
        }

        BaseLoginUser loginUser = SecurityUtil.getLoginUser();
        Set<String> permission = SecurityUtil.getPermission();
        TokenPair tokenPair = tokenService.login(loginUser, permission);

        qrTokenResponse.setStatus(QrCodeStatusEnum.CONFIRM.getValue());
        qrTokenResponse.setAccessToken(tokenPair.accessToken());
        qrTokenResponse.setRefreshToken(tokenPair.refreshToken());
        redisTemplate.opsForValue().set(key, qrTokenResponse, Duration.ofSeconds(60));
        return Result.ok();
    }

    @Override
    public Result<QrTokenResponse> pollstatus(QrCodeIdRequest request) {
        String qrcodeId = request.getQrcodeId();
        Object value = redisTemplate.opsForValue().get(QrCodeConstant.LOGIN_QRCODE_CACHE + qrcodeId);
        QrTokenResponse qrTokenResponse = value instanceof QrTokenResponse ? (QrTokenResponse) value : null;
        if (Objects.isNull(qrTokenResponse)) {
            return Result.ok(QrTokenResponse.OVERDUE());
        }
        return Result.ok(qrTokenResponse);
    }

}