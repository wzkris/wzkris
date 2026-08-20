package com.wzkris.common.web.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;

import java.util.ArrayList;
import java.util.List;

@Data
@RefreshScope
@ConfigurationProperties(prefix = "controller-log")
public class ControllerLogProperties {

    private List<String> ignoreUrls = new ArrayList<>();

}
