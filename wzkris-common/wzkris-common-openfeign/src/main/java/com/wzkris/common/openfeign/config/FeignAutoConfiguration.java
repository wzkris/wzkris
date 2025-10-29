package com.wzkris.common.openfeign.config;

import com.wzkris.common.openfeign.handler.RpcExceptionMvcHandler;
import com.wzkris.common.openfeign.handler.RpcExceptionWebfluxHandler;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.context.annotation.Import;

@Import({FeignClientProperties.class, OpenFeignConfig.class,
        RpcExceptionMvcHandler.class, RpcExceptionWebfluxHandler.class})
@AutoConfiguration
public class FeignAutoConfiguration {

}
