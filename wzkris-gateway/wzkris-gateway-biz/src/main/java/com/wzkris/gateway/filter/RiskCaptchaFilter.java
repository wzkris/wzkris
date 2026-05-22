package com.wzkris.gateway.filter;

import com.wzkris.common.core.constant.CustomHeaderConstants;
import com.wzkris.common.core.enums.BizBaseCodeEnum;
import com.wzkris.common.core.model.Result;
import com.wzkris.common.core.utils.JsonUtil;
import com.wzkris.common.core.utils.ServletUtil;
import com.wzkris.common.core.utils.StringUtil;
import com.wzkris.gateway.domain.RiskCaptchaRequired;
import com.wzkris.gateway.properties.RiskCaptchaProperties;
import com.wzkris.gateway.service.RiskPassJwtValidateService;
import com.wzkris.gateway.utils.PathMatchUtil;
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

@Slf4j
@Order(-90)
@Component
@RequiredArgsConstructor
public class RiskCaptchaFilter extends OncePerRequestFilter {

    private static final AntPathMatcher PATH_MATCHER = new AntPathMatcher();

    private final RiskCaptchaProperties riskCaptchaProperties;

    private final StringRedisTemplate stringRedisTemplate;

    private final RiskPassJwtValidateService riskPassJwtValidateService;

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

        String clientIp = ServletUtil.getClientIP(request);

        boolean locked = stringRedisTemplate.hasKey(clientIp);
        boolean enforced = isPathMatched(riskCaptchaProperties.getEnforcedPaths(), path);

        if (!locked && !enforced) {
            filterChain.doFilter(request, response);
            return;
        }

        String riskPassToken = request.getHeader(CustomHeaderConstants.RISK_PASS_HEADER);
        if (riskPassJwtValidateService.isValid(riskPassToken, clientIp)) {
            filterChain.doFilter(request, response);
            return;
        }

        response.setStatus(HttpStatus.TOO_MANY_REQUESTS.value());
        response.setContentType("application/json;charset=UTF-8");
        response.getWriter().write(JsonUtil.toJsonString(
                Result.init(BizBaseCodeEnum.TOO_MANY_REQUESTS.value(),
                        RiskCaptchaRequired.builder()
                                .riskCaptchaRequired(true)
                                .riskLocked(locked)
                                .build(),
                        "风险验证码缺失或验证失败")));
    }

}
