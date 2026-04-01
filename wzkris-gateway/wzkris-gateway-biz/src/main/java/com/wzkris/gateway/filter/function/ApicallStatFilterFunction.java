package com.wzkris.gateway.filter.function;

import com.wzkris.common.core.model.BaseLoginUser;
import com.wzkris.common.security.utils.SecurityUtil;
import com.wzkris.gateway.domain.StatisticsKey;
import com.wzkris.gateway.service.StatisticsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.function.HandlerFilterFunction;
import org.springframework.web.servlet.function.HandlerFunction;
import org.springframework.web.servlet.function.ServerRequest;
import org.springframework.web.servlet.function.ServerResponse;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * API 调用量统计过滤器（Gateway MVC HandlerFilterFunction 实现）。
 *
 * @author wzkris
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class ApicallStatFilterFunction implements HandlerFilterFunction<ServerResponse, ServerResponse> {

    private final StatisticsService statisticsService;

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

        ServerResponse response = next.handle(request);

        try {
            if (SecurityUtil.isAuth()) {
                int status = response.statusCode().value();
                boolean success = status >= 200 && status < 300;
                BaseLoginUser loginUser = SecurityUtil.getLoginUser();
                recordApiCallStatistics(path, success, loginUser);
            }
        } catch (Exception e) {
            log.warn("接口调用量统计失败: {}", e.getMessage());
        }

        return response;
    }

    private void recordApiCallStatistics(String path, boolean success, BaseLoginUser userInfo) {
        LocalDateTime now = LocalDateTime.now();
        String dateStr = now.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        String hourStr = now.format(DateTimeFormatter.ofPattern("yyyy-MM-dd-HH"));

        StatisticsKey key = StatisticsKey.builder()
                .authType(userInfo.getAuthType().getValue())
                .userId(userInfo.getUid())
                .path(path)
                .date(dateStr)
                .hour(hourStr)
                .build();

        statisticsService.recordApiCallStatistics(key, success);
    }

}
