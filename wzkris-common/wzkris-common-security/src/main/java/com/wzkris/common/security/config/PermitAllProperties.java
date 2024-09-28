package com.wzkris.common.security.config;


import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;

import java.util.List;

/**
 * @author : wzkris
 * @version : V1.0.0
 * @description : 放行URL配置
 * @date : 2024/5/16 16:57
 */
@Data
@RefreshScope
@ConfigurationProperties(prefix = "security.ignore")
public class PermitAllProperties {
    /**
     * 公共配置
     */
    private List<String> commons;

    /**
     * 自定义配置
     */
    private List<String> customs;
}
