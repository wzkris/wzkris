package com.wzkris.gateway.config;

import com.wzkris.gateway.filter.ApicallStatisticsFilter;
import com.wzkris.gateway.filter.AuthenticationFilter;
import com.wzkris.gateway.filter.RouteDecisionFilter;
import com.wzkris.gateway.filter.XssFilter;
import com.wzkris.gateway.utils.ScanAnnotationUrlUtil;
import jakarta.annotation.security.PermitAll;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.function.RouterFunction;
import org.springframework.web.servlet.function.ServerResponse;

import java.util.HashSet;
import java.util.Set;

/**
 * Gateway MVC 认证与过滤器配置：放行路径 Bean、用 {@link org.springframework.web.servlet.function.HandlerFilterFunction} 链包装网关路由。
 *
 * @author wzkris
 */
@Configuration
public class GatewayConfiguration {

    /**
     * 放行路径（带 @PermitAll 的 URL），由 {@link #permitAllPathsPopulator(Set)} 在启动时填充。
     */
    @Bean
    public Set<String> permitAllAnnotations() {
        return new HashSet<>();
    }

    @Bean
    public ApplicationRunner permitAllPathsPopulator(Set<String> permitAllAnnotations) {
        return args -> permitAllAnnotations.addAll(ScanAnnotationUrlUtil.scanUrls(PermitAll.class));
    }

    /**
     * 用认证、路由决策、XSS、统计等过滤器链包装网关的 RouterFunction。
     * 执行顺序：认证 → 路由决策 → XSS → 统计 → 下游转发。
     */
    @Bean
    public RouterFunction<ServerResponse> filteredGatewayRouter(
            ObjectProvider<RouterFunction<ServerResponse>> routerProvider,
            AuthenticationFilter authFilter,
            RouteDecisionFilter routeDecisionFilter,
            XssFilter xssFilter,
            ApicallStatisticsFilter statisticsFilter) {
        RouterFunction<ServerResponse> router = routerProvider.getIfUnique();
        if (router == null) {
            return null;
        }
        return router
                .filter(authFilter)
                .filter(routeDecisionFilter)
                .filter(xssFilter)
                .filter(statisticsFilter);
    }

}
