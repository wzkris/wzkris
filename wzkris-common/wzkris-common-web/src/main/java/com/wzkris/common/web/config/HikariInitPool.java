package com.wzkris.common.web.config;

import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.annotation.Bean;

import javax.sql.DataSource;
import java.sql.Connection;

/**
 * @author : wzkris
 * @version : V1.0.0
 * @description : 初始化连接池
 * @date : 2024/2/22 16:48
 */
public class HikariInitPool {

    /**
     * 项目启动时强制getConnection，来达到启动时hikari创建连接池的目的
     */
    @Bean
    @ConditionalOnBean(DataSource.class)
    public ApplicationRunner validateDataSource(DataSource dataSource) {
        return args -> {
            try (Connection connection = dataSource.getConnection()) {
            }
        };
    }
}
