package com.wzkris.common.thread.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;

/**
 * 动态线程池配置
 *
 * @author wzkris
 * @date 2025/8/8
 */
@Data
@RefreshScope
@Configuration
@ConfigurationProperties("dynamic-threadpool")
public class TpProperties {

    private List<ExecutorProperties> tpExecutors = new ArrayList<>();

    private ExecutorProperties tomcatExecutor;

}
