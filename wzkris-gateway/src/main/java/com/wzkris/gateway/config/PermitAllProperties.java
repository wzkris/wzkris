package com.wzkris.gateway.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author : wzkris
 * @version : V1.0.0
 * @description : 放行URL配置
 * @date : 2024/09/28 16:20
 */
@Data
@Component
@RefreshScope
@ConfigurationProperties(prefix = "security")
public class PermitAllProperties {

    private List<String> ignores;
}
