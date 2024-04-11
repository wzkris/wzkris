package com.thingslink.common.swagger.config;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

/**
 * @author wzkris
 */
@Configuration
@ConditionalOnProperty(name = "springdoc.enabled", matchIfMissing = true)
public class SwaggerConfig {

    private final SwaggerProperties swaggerProperties;

    public SwaggerConfig(SwaggerProperties swaggerProperties) {
        this.swaggerProperties = swaggerProperties;
    }

    @Bean
    public GroupedOpenApi groupedOpenApi(SwaggerProperties swaggerProperties) {
        return GroupedOpenApi.builder()
                .displayName(swaggerProperties.getTitle())
                .group("default") // api发现的默认名称
                .pathsToMatch("/**")
                .addOpenApiMethodFilter(it -> it.getAnnotation(Operation.class) != null)
                .build();
    }


    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI().info(
                new Info().title(swaggerProperties.getTitle())
                        .description(swaggerProperties.getDescription())
                        .license(new License().url(swaggerProperties.getLicense()))
                        .version(swaggerProperties.getVersion())
                        .termsOfService("gg bom") //API服务条款
        );
    }


}
