package com.wzkris.gateway.filter.route;

import com.wzkris.common.core.model.UserPrincipal;
import com.wzkris.gateway.domain.StatisticsKey;
import com.wzkris.gateway.service.StatisticsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * @author : wzkris
 * @version : V1.0.0
 * @description : API调用量统计过滤器
 * @date : 2025/1/15
 */
@Slf4j
@Order(0)
@Component
@RequiredArgsConstructor
public class ApicallStatisticsFilter implements GlobalFilter {

    private final StatisticsService statisticsService;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        String path = exchange.getRequest().getPath().value();

        if (shouldSkipStatistics(path)) {
            return chain.filter(exchange);
        }

        // 先获取认证主体并缓存，然后执行过滤链，最后处理统计（*本统计为API调用量*）
        return exchange.getPrincipal()
                .map(p -> (UserPrincipal) p)
                .doOnNext(principal -> exchange.getAttributes().put("STAT_PRINCIPAL", principal))
                .then(chain.filter(exchange))
                .then(Mono.defer(() -> {
                    // 从属性中获取缓存的用户信息
                    UserPrincipal userInfo = (UserPrincipal) exchange.getAttributes().get("STAT_PRINCIPAL");
                    if (userInfo != null) {
                        try {
                            // 判断请求是否成功（根据响应状态码）
                            boolean success = exchange.getResponse().getStatusCode().is2xxSuccessful();
                            recordApiCallStatistics(path, success, userInfo);
                        } catch (Exception e) {
                            log.warn("接口调用量统计失败: {}", e.getMessage());
                        }
                    }
                    return Mono.empty();
                }));
    }

    /**
     * 记录接口调用量统计
     */
    private void recordApiCallStatistics(String path, boolean success, UserPrincipal userInfo) {
        // 获取时间信息
        LocalDateTime now = LocalDateTime.now();
        String dateStr = now.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        String hourStr = now.format(DateTimeFormatter.ofPattern("yyyy-MM-dd-HH"));

        StatisticsKey key = StatisticsKey.builder()
                .authType(userInfo.getType())
                .userId(userInfo.getId())
                .path(path)
                .date(dateStr)
                .hour(hourStr)
                .build();

        // 异步记录接口调用量统计
        statisticsService.recordApiCallStatistics(key, success);
    }

    /**
     * 判断是否跳过统计
     */
    private boolean shouldSkipStatistics(String path) {
        // 跳过健康检查、监控等路径
        return path.startsWith("/actuator") ||
                path.startsWith("/health") ||
                path.startsWith("/metrics") ||
                path.startsWith("/swagger") ||
                path.startsWith("/doc.html") ||
                path.startsWith("/webjars");
    }

}

