package com.demo.pgbus;

import com.demo.pgbus.config.PgBusProperties;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.ApplicationPidFileWriter;
import org.springframework.boot.context.metrics.buffering.BufferingApplicationStartup;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@MapperScan("com.demo.pgbus.dal.mapper")
@EnableConfigurationProperties(PgBusProperties.class)
@EnableAsync
public class DemoPgBusApplication {

    public static void main(String[] args) {
        SpringApplication springApplication = new SpringApplication(DemoPgBusApplication.class);
        springApplication.addListeners(new ApplicationPidFileWriter());
        springApplication.setApplicationStartup(new BufferingApplicationStartup(2048));
        springApplication.run(args);
        System.out.println("""
                (♥◠‿◠)ﾉﾞ  PostgreSQL LISTEN/NOTIFY demo 启动成功   ლ(´ڡ`ლ)ﾞ
                """);
    }

}

