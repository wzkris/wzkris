package com.wzkris.common.orm.plus.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Configuration;

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
     * 租户表
     */
    private List<String> includes;


    public List<String> getIncludes() {
        return includes == null ? Collections.emptyList() : includes;
    }
}
