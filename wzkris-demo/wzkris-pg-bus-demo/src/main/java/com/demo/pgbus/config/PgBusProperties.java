package com.demo.pgbus.config;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Data;

@Data
@ConfigurationProperties(prefix = "demo.pg-bus")
public class PgBusProperties {

    /**
     * 监听多个 channel 名称列表
     * <p>
     * 如果设置了此列表，将监听列表中的所有 channel
     * 如果未设置或为空，则使用 channel 字段的值
     * </p>
     */
    private List<String> channels;

    /**
     * 是否启用 LISTEN 容器
     */
    private boolean listenerEnabled = true;

    /**
     * 监听线程断线重连前的等待时间
     */
    private Duration reconnectDelay = Duration.ofSeconds(3);

}

