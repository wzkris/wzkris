package com.wzkris.gateway.filter.function;

import com.wzkris.common.core.enums.AuthTypeEnum;
import com.wzkris.common.core.model.BaseLoginUser;
import com.wzkris.common.security.utils.SecurityUtil;
import com.wzkris.gateway.domain.ApiCallEventDO;
import com.wzkris.gateway.repository.ApiCallRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.function.HandlerFilterFunction;
import org.springframework.web.servlet.function.HandlerFunction;
import org.springframework.web.servlet.function.ServerRequest;
import org.springframework.web.servlet.function.ServerResponse;

import java.time.LocalDateTime;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

@Slf4j
@Component
@RequiredArgsConstructor
public class ApiCallStatFilterFunction implements HandlerFilterFunction<ServerResponse, ServerResponse> {

    private final ApiCallRepository apiCallRepository;

    @Override
    public ServerResponse filter(ServerRequest request, HandlerFunction<ServerResponse> next) throws Exception {
        String path = request.path();
        if (path.startsWith("/actuator")
                || path.startsWith("/health")
                || path.startsWith("/metrics")
                || path.startsWith("/swagger")
                || path.startsWith("/doc")
                || path.startsWith("/v3/api-docs")
                || path.startsWith("/webjars")) {
            return next.handle(request);
        }

        long startNano = System.nanoTime();
        String method = request.methodName().toUpperCase(Locale.ROOT);

        try {
            ServerResponse response = next.handle(request);
            int status = response.statusCode().value();
            record(path, method, status, TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - startNano),
                    status >= 200 && status < 300);
            return response;
        } catch (Exception e) {
            record(path, method, 500, TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - startNano), false);
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
            apiCallRepository.recordApiCallEvent(ApiCallEventDO.builder()
                    .timestamp(LocalDateTime.now())
                    .authType(authType)
                    .path(path)
                    .method(method)
                    .userId(userId)
                    .success(success)
                    .statusCode(statusCode)
                    .costMs(costMs)
                    .build());
        } catch (Exception e) {
            log.warn("接口调用量统计失败: {}", e.getMessage());
        }
    }

}
