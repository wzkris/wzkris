package com.wzkris.common.security.config;

import java.util.ArrayList;
import java.util.List;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;

/**
 * @author : wzkris
 * @version : V1.0.0
 * @description : 放行URL配置
 * @date : 2024/5/16 16:57
 */
@Data
@RefreshScope
@ConfigurationProperties(prefix = "security")
public class PermitAllProperties {

    private List<String> ignores = new ArrayList<>();
}
