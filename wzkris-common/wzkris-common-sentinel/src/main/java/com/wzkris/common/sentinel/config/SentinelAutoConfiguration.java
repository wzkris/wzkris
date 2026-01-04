package com.wzkris.common.sentinel.config;

import com.wzkris.common.sentinel.handler.SentinelExceptionHandler;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.context.annotation.Import;

@Import({SentinelExceptionHandler.class, AlarmListenerConfiguration.class})
@AutoConfiguration
public class SentinelAutoConfiguration {

}
