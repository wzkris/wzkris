package com.thingslink.common.security.config.white;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;

/**
 * @author : wzkris
 * @version : V1.0.0
 * @description : 模块下的放行白名单
 * @date : 2023/11/27 13:41
 */
@Data
@RefreshScope
@Configuration
@ConfigurationProperties(prefix = "security.custom-white")
public class CustomWhiteConfig {

    private List<String> urls = new ArrayList<>();
}
