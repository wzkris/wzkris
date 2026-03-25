package com.wzkris.common.remote.config;

import com.wzkris.common.remote.annotation.EnableRemoteInterfaces;
import com.wzkris.common.remote.properties.RemoteInterfaceProperties;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

/**
 * 自动配置 Remote Interface clients.
 */
@EnableConfigurationProperties({RemoteInterfaceProperties.class})
@EnableRemoteInterfaces
@AutoConfiguration
public class RemoteInterfaceAutoConfiguration {

}
