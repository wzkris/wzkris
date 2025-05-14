package com.wzkris.common.oss.config;

import lombok.Data;
import lombok.experimental.FieldNameConstants;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * 对象配置信息
 *
 * @author wzkris
 */
@Data
@FieldNameConstants
@RefreshScope
@Configuration
@ConfigurationProperties(prefix = "oss")
public class OssConfig {

    private String use = "minio";

    private LocalProperties local;

    private MinioProperties minio;

    private FastDfsProperties fdfs;

    @Data
    public static class LocalProperties {

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

    @Data
    public static class MinioProperties {

        /**
         * 服务地址
         */
        private String url;

        /**
         * 用户名
         */
        private String accessKey;

        /**
         * 密码
         */
        private String secretKey;
    }

    @Data
    public static class FastDfsProperties {

        /**
         * 服务地址
         */
        private String domain;

        /**
         * 用户名
         */
        private Integer soTimeout;

        /**
         * 密码
         */
        private Integer connectTimeout;

        /**
         * tracker ip列表
         */
        private List<String> trackerList;
    }
}
