package com.wzkris.gateway.filter.function;

import com.wzkris.common.core.enums.AuthTypeEnum;
import com.wzkris.common.core.model.BaseLoginUser;
import com.wzkris.common.security.utils.SecurityUtil;
import com.wzkris.gateway.domain.ApiCallStatKey;
import com.wzkris.gateway.service.ApiCallStatWriteService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.function.HandlerFilterFunction;
import org.springframework.web.servlet.function.HandlerFunction;
import org.springframework.web.servlet.function.ServerRequest;
import org.springframework.web.servlet.function.ServerResponse;

import java.util.Locale;
import java.util.concurrent.TimeUnit;

/**
 * API 调用量统计过滤器（Gateway MVC HandlerFilterFunction 实现）。
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class ApiCallStatFilterFunction implements HandlerFilterFunction<ServerResponse, ServerResponse> {

    private final ApiCallStatWriteService writeService;

    private static boolean shouldSkipStatistics(String path) {
        return path.startsWith("/actuator")
                || path.startsWith("/health")
                || path.startsWith("/metrics")
                || path.startsWith("/swagger")
                || path.startsWith("/doc")
                || path.startsWith("/v3/api-docs")
                || path.startsWith("/webjars");
    }

    @Override
    public ServerResponse filter(ServerRequest request, HandlerFunction<ServerResponse> next) throws Exception {
        String path = request.path();
        if (shouldSkipStatistics(path)) {
            return next.handle(request);
        }

        long startNano = System.nanoTime();
        String method = request.methodName().toUpperCase(Locale.ROOT);

        try {
            ServerResponse response = next.handle(request);
            int status = response.statusCode().value();
            boolean success = status >= 200 && status < 300;
            long costMs = TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - startNano);
            record(path, method, status, costMs, success);
            return response;
        } catch (Exception e) {
            long costMs = TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - startNano);
            record(path, method, 500, costMs, false);
            throw e;
        }
    }

    private void record(String path, String method, int statusCode, long costMs, boolean success) {
        try {
            AuthTypeEnum authType = null;
            Long userId = null;
            if (SecurityUtil.isAuth()) {
                BaseLoginUser loginUser = SecurityUtil.getLoginUser();
                authType = loginUser.getAuthType();
                userId = loginUser.getUid();
            }

            ApiCallStatKey key = ApiCallStatKey.builder()
                    .authType(authType)
                    .userId(userId)
                    .path(path)
                    .method(method)
                    .statusCode(statusCode)
                    .costMs(costMs)
                    .build();
            writeService.recordApiCall(key, success);
        } catch (Exception e) {
            log.warn("接口调用量统计失败: {}", e.getMessage());
        }
    }

}
