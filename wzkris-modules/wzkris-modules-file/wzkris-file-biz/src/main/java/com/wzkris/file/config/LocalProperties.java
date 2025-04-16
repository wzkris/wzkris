package com.wzkris.file.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Configuration;

/**
 * @author : wzkris
 * @version : V1.0.0
 * @description : 本地配置
 * @date : 2023/9/8 16:02
 */
@Data
@RefreshScope
@Configuration
@ConfigurationProperties(prefix = "local")
public class LocalProperties {

    /**
     * 域名前缀
     */
    public String domain;

    /**
     * 资源映射路径 前缀
     */
    public String prefix;

    /**
     * 存储地址
     */
    private String path;
}
