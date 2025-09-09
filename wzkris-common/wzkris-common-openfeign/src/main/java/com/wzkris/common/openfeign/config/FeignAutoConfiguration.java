package com.wzkris.common.openfeign.config;

import com.wzkris.common.openfeign.handler.FeignExceptionHandler;
import com.wzkris.common.openfeign.handler.RpcExceptionHandler;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.context.annotation.Import;

@Import({FeignClientProperties.class, OpenFeignConfig.class,
        FeignExceptionHandler.class, RpcExceptionHandler.class})
@AutoConfiguration
public class FeignAutoConfiguration {

}
