package com.wzkris.common.loadbalancer.config;

import com.wzkris.common.loadbalancer.enums.RoutePolicyStatusEnum;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;

/**
 * 路由策略配置
 *
 * @author wzkris
 */
@RefreshScope
@ConfigurationProperties("route-policy")
@Data
public class RoutePolicyProperties {

    /**
     * @see RoutePolicyStatusEnum
     */
    private String status = RoutePolicyStatusEnum.CLOSE.getValue();

    /**
     * 强制路由，RoutePolicyStatusEnum.FORCE下生效
     */
    private String forceHint;

    /**
     * 默认路由，RoutePolicyStatusEnum.OPEN当hint=null或""时生效
     */
    private String defaultHint;

}
