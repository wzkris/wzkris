package com.wzkris.gateway.config;

import com.wzkris.gateway.filter.function.ApicallStatFilterFunction;
import com.wzkris.gateway.filter.function.RouteDecisionFilterFunction;
import com.wzkris.gateway.filter.function.SecurityContextFilterFunction;
import com.wzkris.gateway.filter.function.XssFilterFunction;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.function.RouterFunction;
import org.springframework.web.servlet.function.ServerResponse;

/**
 * Gateway MVC 认证与过滤器配置：放行路径 Bean、用 {@link org.springframework.web.servlet.function.HandlerFilterFunction} 链包装网关路由。
 *
 * @author wzkris
 */
@Configuration
public class GatewayConfiguration {

    /**
     * 用请求头追加、路由决策、XSS、统计等过滤器链包装网关的 RouterFunction。
     * 执行顺序：追加请求头 → 路由决策 → XSS → 统计 → 下游转发。
     */
    @Bean
    public RouterFunction<ServerResponse> filteredGatewayRouter(
            ObjectProvider<RouterFunction<ServerResponse>> routerProvider,
            SecurityContextFilterFunction securityContextFilterFunction,
            RouteDecisionFilterFunction routeDecisionFilterFunction,
            XssFilterFunction xssFilterFunction,
            ApicallStatFilterFunction apicallStatFilterFunction) {
        RouterFunction<ServerResponse> router = routerProvider.getIfUnique();
        if (router == null) {
            return null;
        }
        return router
                .filter(securityContextFilterFunction)
                .filter(routeDecisionFilterFunction)
                .filter(xssFilterFunction)
                .filter(apicallStatFilterFunction);
    }

}
