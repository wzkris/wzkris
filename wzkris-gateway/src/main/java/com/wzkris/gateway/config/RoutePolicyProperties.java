package com.wzkris.gateway.config;

import com.wzkris.common.loadbalancer.enums.RoutePolicyEnum;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Configuration;

/**
 * 路由策略配置
 *
 * @author wzkris
 */
@RefreshScope
@Configuration
@ConfigurationProperties("route-decision")
@Data
public class RoutePolicyProperties {

    /**
     * 路由策略
     */
    private RoutePolicyEnum policy = RoutePolicyEnum.CLOSE;

    /**
     * 强制路由配置，RoutePolicyEnum.FORCE生效
     */
    private ForceConfig forceConfig = new ForceConfig();

    /**
     * 正常路由配置，RoutePolicyEnum.OPEN生效
     */
    private OpenConfig openConfig = new OpenConfig();

    /**
     * 强制路由配置
     */
    @Data
    public static class ForceConfig {

        /**
         * 强制路由的hint值
         */
        private String hintValue;

    }

    /**
     * 默认路由配置
     */
    @Data
    public static class OpenConfig {

        /**
         * 若不存在hint值，则使用此默认的hint值
         */
        private String defaultHintValue;

    }

}
