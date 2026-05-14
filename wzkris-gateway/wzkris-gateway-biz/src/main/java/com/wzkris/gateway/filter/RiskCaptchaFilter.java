package com.wzkris.gateway.filter;

import com.wzkris.captcha.properties.RiskPassProperties;
import com.wzkris.captcha.risk.RiskClientKeys;
import com.wzkris.common.core.enums.BizBaseCodeEnum;
import com.wzkris.common.core.model.Result;
import com.wzkris.common.core.utils.JsonUtil;
import com.wzkris.common.core.utils.StringUtil;
import com.wzkris.gateway.properties.PermitUrlProperties;
import com.wzkris.gateway.properties.RiskCaptchaProperties;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Map;

@Slf4j
@Order(-99)
@Component
@RequiredArgsConstructor
public class RiskCaptchaFilter extends OncePerRequestFilter {

    private static final AntPathMatcher PATH_MATCHER = new AntPathMatcher();

    private final PermitUrlProperties permitUrlProperties;

    private final RiskCaptchaProperties riskCaptchaProperties;

    private final RiskPassProperties riskPassProperties;

    private final StringRedisTemplate stringRedisTemplate;

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
        String clientKey = RiskClientKeys.defaultCompositeKey(request);
        boolean locked = Boolean.TRUE.equals(
                stringRedisTemplate.hasKey(riskPassProperties.getLockKeyPrefix() + clientKey));
        boolean enforced = isPathMatched(riskCaptchaProperties.getEnforcedPaths(), path);

        if (isPathMatched(riskCaptchaProperties.getExemptPaths(), path)) {
            filterChain.doFilter(request, response);
            return;
        }

        if (!locked && !enforced
                && riskCaptchaProperties.isPermitIgnoresWhenIdle()
                && isPathMatched(permitUrlProperties.getIgnores(), path)) {
            filterChain.doFilter(request, response);
            return;
        }

        if (!locked && !enforced) {
            filterChain.doFilter(request, response);
            return;
        }

        if (hasValidPass(request, clientKey)) {
            filterChain.doFilter(request, response);
            return;
        }

        if (log.isDebugEnabled()) {
            log.debug("risk captcha blocked path={} locked={} enforced={} clientKey={}", path, locked, enforced, clientKey);
        }
        writeJsonResponse(response, HttpStatus.TOO_MANY_REQUESTS,
                Result.init(BizBaseCodeEnum.TOO_MANY_REQUESTS.value(),
                        Map.of("riskCaptchaRequired", true, "riskLocked", locked, "path", path),
                        "risk.captcha.required"));
    }

    private boolean hasValidPass(HttpServletRequest request, String clientKey) {
        String raw = request.getHeader(riskCaptchaProperties.getPassHeader());
        if (StringUtil.isBlank(raw)) {
            return false;
        }
        String passToken = raw.trim();
        String bound = stringRedisTemplate.opsForValue().get(riskPassProperties.getPassKeyPrefix() + passToken);
        return clientKey.equals(bound);
    }

}
