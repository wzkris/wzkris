package com.wzkris.common.log.config;

import com.wzkris.common.log.annotation.aspect.OperateLogAspect;
import com.wzkris.common.log.event.listener.OperateEventListener;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.context.annotation.Import;

@Import({OperateLogAspect.class, OperateEventListener.class})
@AutoConfiguration
public class LogAutoConfiguration {

}
