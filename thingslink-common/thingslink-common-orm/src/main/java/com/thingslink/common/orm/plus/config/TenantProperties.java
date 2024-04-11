package com.thingslink.common.orm.plus.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;

/**
 * 租户 配置属性
 *
 * @author wzkris
 */
@Data
@RefreshScope
@Configuration
@ConfigurationProperties(prefix = "tenant")
public class TenantProperties {

    /**
     * 排除表
     */
    private List<String> excludes;


    public List<String> getExcludes() {
        return excludes == null ? Collections.emptyList() : excludes;
    }
}
