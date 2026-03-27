package com.wzkris.gateway.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * @author : wzkris
 * @version : V1.0.0
 * @description : 放行URL配置
 * @date : 2024/09/28 16:20
 */
@Data
@Component
@RefreshScope
@ConfigurationProperties(prefix = "security")
public class PermitAllProperties {

    /**
     * 白名单
     */
    private List<String> ignores = new ArrayList<>();

    /**
     * 黑名单
     */
    private List<String> denys = new ArrayList<>();

    /**
     * 是否允许 WebSocket 握手阶段通过 query 参数 access_token 传递 token。
     * <p>
     * 说明：浏览器 WebSocket 无法自定义请求头，若关闭该开关则需要其它传递方式（例如子协议/首帧认证）。
     * </p>
     */
    private boolean wsQueryTokenEnabled = true;

}
