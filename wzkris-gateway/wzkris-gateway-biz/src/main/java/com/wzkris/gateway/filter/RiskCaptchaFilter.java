package com.wzkris.gateway.filter;

import com.wzkris.common.core.constant.CustomHeaderConstants;
import com.wzkris.common.core.enums.BizBaseCodeEnum;
import com.wzkris.common.core.model.Result;
import com.wzkris.common.core.utils.JsonUtil;
import com.wzkris.common.core.utils.StringUtil;
import com.wzkris.gateway.properties.RiskCaptchaProperties;
import com.wzkris.gateway.response.RiskCaptchaRequiredResponse;
import com.wzkris.gateway.service.GatewayRiskLockService;
import com.wzkris.gateway.service.GatewayRiskPassService;
import com.wzkris.gateway.utils.GatewayRiskClientKeys;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j
@Order(-90)
@Component
@RequiredArgsConstructor
public class RiskCaptchaFilter extends OncePerRequestFilter {

    private static final AntPathMatcher PATH_MATCHER = new AntPathMatcher();

    private final RiskCaptchaProperties riskCaptchaProperties;

    private final GatewayRiskLockService gatewayRiskLockService;

    private final GatewayRiskPassService gatewayRiskPassService;

    private static void writeJsonResponse(HttpServletResponse response, HttpStatus status, Object body)
            throws IOException {
        response.setStatus(status.value());
        response.setContentType("application/json;charset=UTF-8");
        response.getWriter().write(JsonUtil.toJsonString(body));
    }

    private static boolean isPathMatched(Iterable<String> patterns, String path) {
        if (patterns == null) {
            return false;
        }
        for (String pattern : patterns) {
            if (StringUtil.isNotBlank(pattern) && PATH_MATCHER.match(pattern, path)) {
                return true;
            }
        }
        return false;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        if (!riskCaptchaProperties.isEnabled()) {
            filterChain.doFilter(request, response);
            return;
        }

        String path = request.getRequestURI();
        String clientKey = GatewayRiskClientKeys.defaultCompositeKey(request);

        boolean locked = gatewayRiskLockService.isLocked(request);
        boolean enforced = isPathMatched(riskCaptchaProperties.getEnforcedPaths(), path);

        if (!locked && !enforced) {
            filterChain.doFilter(request, response);
            return;
        }

        if (hasValidPass(request, clientKey)) {
            filterChain.doFilter(request, response);
            return;
        }

        writeJsonResponse(response, HttpStatus.TOO_MANY_REQUESTS,
                Result.init(BizBaseCodeEnum.TOO_MANY_REQUESTS.value(),
                        RiskCaptchaRequiredResponse.builder()
                                .riskCaptchaRequired(true)
                                .riskLocked(locked)
                                .path(path)
                                .build(),
                        "risk.captcha.required"));
    }

    private boolean hasValidPass(HttpServletRequest request, String clientKey) {
        String raw = request.getHeader(CustomHeaderConstants.RISK_PASS_HEADER);
        if (StringUtil.isBlank(raw)) {
            return false;
        }
        return gatewayRiskPassService.validatePass(raw.trim(), clientKey);
    }

}
