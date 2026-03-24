package com.wzkris.auth.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@RefreshScope
@ConfigurationProperties(prefix = "login-risk")
public class LoginRiskProperties {

    /**
     * 失败次数阈值（命中 HIGH_FAIL_FREQ）
     */
    private int highFailFreqThreshold = 5;

    /**
     * 失败统计窗口（分钟）
     */
    private int failWindowMinutes = 10;

    /**
     * IP 漂移比较窗口（分钟）
     */
    private int ipDriftWindowMinutes = 30;

    /**
     * UA 变化比较窗口（分钟）
     */
    private int uaMutationWindowMinutes = 30;

    /**
     * 非常用时段开始小时（含）
     */
    private int offHoursStart = 0;

    /**
     * 非常用时段结束小时（不含）
     */
    private int offHoursEnd = 6;

    /**
     * 告警抑制时长（分钟）
     */
    private int alertSuppressMinutes = 10;

}
