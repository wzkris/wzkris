package com.wzkris.common.swagger.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "springdoc")
public class SwaggerProperties {

    /**
     * 是否开启 swagger / knife4j
     */
    private Boolean enabled = false;

    /**
     * OpenAPI 分组名，文档地址为 {@code /v3/api-docs/{apiGroup}}
     */
    private String apiGroup = "default";

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
    private String termsOfServiceUrl = "内部文档，禁止外泄";

}
