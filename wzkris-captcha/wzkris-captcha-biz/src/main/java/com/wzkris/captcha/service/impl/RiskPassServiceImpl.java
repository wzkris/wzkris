package com.wzkris.captcha.service.impl;

import com.wzkris.auth.remote.api.jwt.request.ServiceJwtIssueRequest;
import com.wzkris.auth.remote.api.jwt.response.ServiceJwtIssueResponse;
import com.wzkris.captcha.enums.CaptchaTypeEnum;
import com.wzkris.captcha.properties.RiskPassProperties;
import com.wzkris.captcha.remote.auth.IServiceJwtIssueRemote;
import com.wzkris.captcha.request.RiskPassExchangeRequest;
import com.wzkris.captcha.response.RiskPassExchangeResponse;
import com.wzkris.captcha.service.ChallengeService;
import com.wzkris.captcha.service.ImageCaptchaService;
import com.wzkris.captcha.service.SlideCaptchaService;
import com.wzkris.common.core.constant.JwtClaimConstants;
import com.wzkris.common.core.model.Result;
import com.wzkris.common.core.utils.ResultUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class RiskPassServiceImpl {

    private static final String GATEWAY_RISK_LOCK_PREFIX = "gateway:risk-lock:";

    private final ImageCaptchaService imageCaptchaService;

    private final SlideCaptchaService slideCaptchaService;

    private final ChallengeService challengeService;

    private final IServiceJwtIssueRemote serviceJwtIssueRemote;

    private final RiskPassProperties riskPassProperties;

    private final StringRedisTemplate stringRedisTemplate;

    public Result<RiskPassExchangeResponse> exchange(RiskPassExchangeRequest request, String clientKey) {
        if (!validateExchange(request)) {
            return Result.requestFail("invalidParameter.captcha.error");
        }
        Map<String, Object> claims = new HashMap<>();
        claims.put(JwtClaimConstants.TOKEN_TYPE, JwtClaimConstants.TOKEN_TYPE_RISK_PASS);
        ServiceJwtIssueRequest issueRequest = new ServiceJwtIssueRequest();
        issueRequest.setSubject(clientKey);
        issueRequest.setClaims(claims);
        issueRequest.setTtlSeconds(riskPassProperties.getTtlSeconds());
        Result<ServiceJwtIssueResponse> issued = serviceJwtIssueRemote.issue(issueRequest);
        if (!ResultUtil.check(issued)) {
            return Result.requestFail(issued.getMessage());
        }
        stringRedisTemplate.delete(GATEWAY_RISK_LOCK_PREFIX + clientKey);
        var data = issued.getData();
        return Result.ok(RiskPassExchangeResponse.of(
                data.getToken(),
                data.getExpiresAt(),
                request.getCaptchaType().getValue()));
    }

    private boolean validateExchange(RiskPassExchangeRequest request) {
        return Boolean.TRUE.equals(validateToken(
                request.getCaptchaType(),
                request.getCaptchaToken().trim()));
    }

    private Boolean validateToken(CaptchaTypeEnum captchaType, String token) {
        return switch (Objects.requireNonNull(captchaType)) {
            case CHALLENGE -> challengeService.validateToken(token);
            case IMAGE -> imageCaptchaService.validateToken(token);
            case SLIDE -> slideCaptchaService.validateToken(token);
        };
    }

}
