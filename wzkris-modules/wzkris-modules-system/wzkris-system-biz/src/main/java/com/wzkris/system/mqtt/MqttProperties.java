package com.wzkris.system.mqtt;

import java.util.HashMap;
import java.util.Map;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Configuration;

/**
 * @author : wzkris
 * @version : V1.0.0
 * @description : mqtt配置属性
 * @date : 2023/4/29 14:29
 * @update: 2024/11/16 14:45
 */
@Data
@RefreshScope
@Configuration
@ConfigurationProperties(prefix = "mqtt")
public class MqttProperties {

    /**
     * 状态
     */
    private Boolean enable = false;

    /**
     * 配置
     */
    private Map<String, GeneralSetting> settings = new HashMap<>();

    /**
     * 通用配置
     */
    @Data
    public static class GeneralSetting {

        /**
         * MQTT客户端数量
         */
        private int clientNum = 1;

        /**
         * MQTT连接地址
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
         * 是否自动重连
         */
        private Boolean automaticReconnect = true;

        /**
         * 最大重连间隔 单位毫秒 默认2分钟
         */
        private int maxReconnectDelay = 120000;

        /**
         * 是否持久订阅(true-临时订阅 false-持久订阅)
         */
        private Boolean cleanSession = true;

        /**
         * 最大飞行窗口
         */
        private int maxInFlight = 100;

        /**
         * 建立连接的超时时间， 单位秒
         */
        private int connectTimeout = 30;

        /**
         * 心跳机制
         * 此为发送两次心跳的间隔时间，在此期间都视为存活， 单位秒
         */
        private int keepAlive = 120;
    }
}
