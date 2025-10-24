package com.wzkris.gateway.filter;

import com.wzkris.common.core.constant.HeaderConstants;
import com.wzkris.common.core.model.MyPrincipal;
import com.wzkris.common.core.model.domain.LoginCustomer;
import com.wzkris.common.core.model.domain.LoginStaff;
import com.wzkris.common.core.model.domain.LoginUser;
import com.wzkris.common.core.utils.JsonUtil;
import com.wzkris.common.core.utils.StringUtil;
import com.wzkris.gateway.service.StatisticsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.annotation.Order;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * @author : wzkris
 * @version : V1.0.0
 * @description : 统计过滤器（PV/UV统计）
 * @date : 2025/1/15
 */
@Slf4j
@Order(0)
@Component
@RequiredArgsConstructor
public class StatisticsFilter implements GlobalFilter {

    private final StatisticsService statisticsService;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        String path = request.getPath().value();

        // 跳过统计的路径（如健康检查、监控等）
        if (shouldSkipStatistics(path)) {
            return chain.filter(exchange);
        }

        // 异步统计，不影响主流程
        return chain.filter(exchange)
                .doOnSuccess(result -> {
                    try {
                        // 成功响应后统计
                        recordStatistics(exchange, path, true);
                    } catch (Exception e) {
                        log.warn("统计记录失败: {}", e.getMessage());
                    }
                })
                .doOnError(error -> {
                    try {
                        // 失败响应后统计
                        recordStatistics(exchange, path, false);
                    } catch (Exception e) {
                        log.warn("统计记录失败: {}", e.getMessage());
                    }
                });
    }

    /**
     * 记录统计数据
     */
    private void recordStatistics(ServerWebExchange exchange, String path, boolean success) {
        ServerHttpRequest request = exchange.getRequest();

        // 获取时间信息
        LocalDateTime now = LocalDateTime.now();
        String dateStr = now.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        String hourStr = now.format(DateTimeFormatter.ofPattern("yyyy-MM-dd-HH"));

        // 构建统计键
        MyPrincipal userInfo = extractUserInfo(request);
        if (userInfo == null) return;

        StatisticsKey key = StatisticsKey.builder()
                .authType(userInfo.getType())
                .userId(userInfo.getId())
                .path(path)
                .date(dateStr)
                .hour(hourStr)
                .build();

        // 异步记录统计
        statisticsService.recordStatistics(key, success);
    }

    /**
     * 提取用户信息
     */
    private MyPrincipal extractUserInfo(ServerHttpRequest request) {
        // 检查用户信息
        String userInfo = request.getHeaders().getFirst(HeaderConstants.X_USER_INFO);
        if (StringUtil.isNotBlank(userInfo)) {
            try {
                return JsonUtil.parseObject(userInfo, LoginUser.class);
            } catch (Exception e) {
                log.warn("解析用户信息失败: {}", e.getMessage());
            }
        }

        // 检查员工信息
        String staffInfo = request.getHeaders().getFirst(HeaderConstants.X_STAFF_INFO);
        if (StringUtil.isNotBlank(staffInfo)) {
            try {
                return JsonUtil.parseObject(staffInfo, LoginStaff.class);
            } catch (Exception e) {
                log.warn("解析员工信息失败: {}", e.getMessage());
            }
        }

        // 检查客户信息
        String customerInfo = request.getHeaders().getFirst(HeaderConstants.X_CUSTOMER_INFO);
        if (StringUtil.isNotBlank(customerInfo)) {
            try {
                return JsonUtil.parseObject(customerInfo, LoginCustomer.class);
            } catch (Exception e) {
                log.warn("解析客户信息失败: {}", e.getMessage());
            }
        }

        // 匿名用户
        return null;
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

    /**
     * 统计键
     */
    @lombok.Data
    @lombok.Builder
    public static class StatisticsKey {

        private String authType;

        private Long userId;

        private String path;

        private String date;

        private String hour;

    }

}
