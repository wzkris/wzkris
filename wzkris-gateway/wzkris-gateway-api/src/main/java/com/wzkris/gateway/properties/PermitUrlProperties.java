package com.wzkris.gateway.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * 网关安全配置属性
 *
 * @author wzkris
 */
@Data
@Component
@RefreshScope
@ConfigurationProperties(prefix = "security")
public class PermitUrlProperties {

    /**
     * 黑名单路径
     */
    private List<String> denys = new ArrayList<>();

    /**
     * 是否允许WebSocket握手阶段通过query参数传递token
     */
    private boolean wsQueryTokenEnabled = true;

    /**
     * websocket请求地址
     */
    private String wsUri = "";

}
