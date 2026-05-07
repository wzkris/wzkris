package com.wzkris.common.swagger.config;

import com.wzkris.common.swagger.support.OpenApiMethodPredicates;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

/**
 * Knife4j / SpringDoc 公共装配：单分组、仅收录带 {@code io.swagger.v3.oas.annotations.tags.Tag} 的接口。
 */
@EnableConfigurationProperties(SwaggerProperties.class)
@ConditionalOnProperty(name = "springdoc.enabled", matchIfMissing = true)
@AutoConfiguration
public class SwaggerAutoConfiguration {

    private final SwaggerProperties swaggerProperties;

    public SwaggerAutoConfiguration(SwaggerProperties swaggerProperties) {
        this.swaggerProperties = swaggerProperties;
    }

    @Bean
    public GroupedOpenApi groupedOpenApi() {
        return GroupedOpenApi.builder()
                .group(swaggerProperties.getApiGroup())
                .addOpenApiMethodFilter(OpenApiMethodPredicates::hasTag)
                .build();
    }

    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI().info(buildInfo());
    }

    private Info buildInfo() {
        return new Info()
                .title(swaggerProperties.getTitle())
                .description(swaggerProperties.getDescription())
                .license(new License().url(swaggerProperties.getLicense()))
                .version(swaggerProperties.getVersion())
                .termsOfService(swaggerProperties.getTermsOfServiceUrl());
    }

}
