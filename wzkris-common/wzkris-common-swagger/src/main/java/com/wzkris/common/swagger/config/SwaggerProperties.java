package com.wzkris.common.swagger.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @author wzkris
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "springdoc")
public class SwaggerProperties {

    /**
     * 是否开启swagger
     */
    private Boolean enabled = false;

    /**
     * 标题
     **/
    private String title = "";

    /**
     * 描述
     **/
    private String description = "";

    /**
     * 版本
     **/
    private String version = "";

    /**
     * 许可证
     **/
    private String license = "";

    /**
     * 服务条款URL
     **/
    private String termsOfServiceUrl = "内部接口，禁止外泄";

}
