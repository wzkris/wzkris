package com.wzkris.common.swagger.config;

import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;

/**
 * @author wzkris
 */
@ConditionalOnProperty(name = "springdoc.enabled", matchIfMissing = true)
public class SwaggerConfig {

    private final SwaggerProperties swaggerProperties;

    public SwaggerConfig(SwaggerProperties swaggerProperties) {
        this.swaggerProperties = swaggerProperties;
    }

    @Bean
    public GroupedOpenApi groupedOpenApi(SwaggerProperties swaggerProperties) {
        return GroupedOpenApi.builder()
                .group(this.getClass().getName())
                .displayName(swaggerProperties.getTitle())
                .addOpenApiMethodFilter(it -> it.getAnnotation(Tag.class) != null)
                .build();
    }

    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
                .info(
                        new Info()
                                .title(swaggerProperties.getTitle())
                                .description(swaggerProperties.getDescription())
                                .license(new License().url(swaggerProperties.getLicense()))
                                .version(swaggerProperties.getVersion())
                                .termsOfService("gg bom") // API服务条款
                );
    }

}
