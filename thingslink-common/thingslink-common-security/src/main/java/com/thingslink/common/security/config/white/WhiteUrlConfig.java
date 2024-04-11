package com.thingslink.common.security.config.white;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;

/**
 * 公共放行白名单配置
 *
 * @author wzkris
 */
@Data
@RefreshScope
@Configuration
@ConfigurationProperties(prefix = "security.white")
public class WhiteUrlConfig {

    /**
     * 公共配置
     */
    private Common common;

    /**
     * 自定义配置
     */
    private Custom custom;

    public Common getCommon() {
        return common == null ? new Common() : this.common;
    }

    public Custom getCustom() {
        return custom == null ? new Custom() : this.custom;
    }

    @Data
    @NoArgsConstructor
    public static class Common {
        /**
         * 放行白名单配置
         */
        private List<String> urls = new ArrayList<>();
    }

    @Data
    @NoArgsConstructor
    public static class Custom {
        /**
         * 放行白名单配置
         */
        private List<String> urls = new ArrayList<>();
    }
}
