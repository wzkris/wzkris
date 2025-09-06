package com.wzkris.common.log.config;

import com.wzkris.common.log.filter.TracingLogFilter;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.context.annotation.Import;

@Import(TracingLogFilter.class)
@AutoConfiguration
public class LogAutoConfiguration {

}
