package com.wzkris.common.orm.plus.config;

import java.util.Collections;
import java.util.Set;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;

/**
 * 租户 配置属性
 *
 * @author wzkris
 */
@Data
@RefreshScope
@ConfigurationProperties(prefix = "tenant")
public class TenantProperties {

    /**
     * 租户表
     */
    private Set<String> includes;

    public Set<String> getIncludes() {
        return includes == null ? Collections.emptySet() : includes;
    }
}
