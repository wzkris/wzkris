package com.wzkris.common.openfeign.config;

import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.context.annotation.Import;

@Import({FeignClientProperties.class, OpenFeignConfig.class})
@AutoConfiguration
public class FeignAutoConfiguration {

}
