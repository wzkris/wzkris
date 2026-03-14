package com.wzkris.risk.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@RefreshScope
@ConfigurationProperties(prefix = "risk")
public class RiskProperties {

    private int failThreshold = 5;

    private int failWindowSeconds = 600;

    private int ipLimitThreshold = 80;

    private int ipLimitWindowSeconds = 60;

    private int pathBurstThreshold = 30;

    private int pathBurstWindowSeconds = 60;

    private int ipDriftWindowMinutes = 30;

    private int uaMutationWindowMinutes = 30;

    private boolean allowLocalSource = true;

    private boolean blockEmptyUserAgent = false;

}
