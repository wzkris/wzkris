package org.mock.device.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @author : wzkris
 * @version : V1.0.0
 * @description : mqtt配置属性
 * @date : 2023/4/29 14:29
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "mqtt")
public class MqttProperties {
    /**
     * mqtt连接地址
     */
    private String host;
    /**
     * 用户名
     */
    private String username;
    /**
     * 密码
     */
    private String password;
    /**
     * 客户端id
     */
    private String clientId;
    /**
     * 连接超时时间
     */
    private Integer timeout;
    /**
     * 心跳机制
     * 此为发送两次心跳的间隔时间，在此期间都视为存活
     */
    private Integer keepAlive;
}
