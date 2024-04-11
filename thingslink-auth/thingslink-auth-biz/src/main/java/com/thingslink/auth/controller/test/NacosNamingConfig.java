package com.thingslink.auth.controller.test;

import com.alibaba.nacos.api.exception.NacosException;
import com.alibaba.nacos.api.naming.NamingFactory;
import com.alibaba.nacos.api.naming.NamingService;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Properties;

/**
 * @author : wzkris
 * @version : V1.0.0
 * @description :
 * @date : 2023/12/13 10:47
 */

@Configuration
@Data
@Slf4j
public class NacosNamingConfig {

    @Value("${spring.cloud.nacos.server-addr}")
    private String serverAddr;
    @Value("${spring.cloud.nacos.discovery.namespace}")
    private String namespace;
    @Value("${spring.cloud.nacos.username}")
    private String username;
    @Value("${spring.cloud.nacos.password}")
    private String password;


    private NamingService namingService;


    @Bean(name = "namingService")
    public NamingService get() throws NacosException {
        log.info("NacosNamingConfig namingService 执行");
        Properties properties = System.getProperties();
        properties.setProperty("serverAddr", serverAddr);
        properties.setProperty("namespace", namespace);
        properties.setProperty("username", username);
        properties.setProperty("password", password);
        namingService = NamingFactory.createNamingService(properties);
        return namingService;
    }

}
