package com.wzkris.common.thread.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * 线程池配置
 *
 * @author wzkris
 * @date 2025/8/8
 */
@Data
@RefreshScope
@Configuration
@ConfigurationProperties("wzkris-tp")
public class TpProperties {

    private List<ExecutorProperties> tpExecutors;

    private ExecutorProperties tomcatExecutor;

}
