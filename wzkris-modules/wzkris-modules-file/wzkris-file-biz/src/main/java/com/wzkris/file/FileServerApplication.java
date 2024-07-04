package com.wzkris.file;

import com.wzkris.common.web.annotation.EnableCustomConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.context.ApplicationPidFileWriter;
import org.springframework.boot.context.metrics.buffering.BufferingApplicationStartup;

/**
 * 文件服务
 * 暂无数据库服务，排除相关配置
 *
 * @author wzkris
 */
@EnableCustomConfig
@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
public class FileServerApplication {

    public static void main(String[] args) {
        SpringApplication springApplication = new SpringApplication(FileServerApplication.class);
        springApplication.addListeners(new ApplicationPidFileWriter());
        springApplication.setApplicationStartup(new BufferingApplicationStartup(2048));
        springApplication.run(args);
        System.out.println("""
                (♥◠‿◠)ﾉﾞ  文件服务模块启动成功   ლ(´ڡ`ლ)ﾞ \s
                  _   _     _                 _ _       _   \s
                 | | | |   (_)               | (_)     | |  \s
                 | |_| |__  _ _ __   __ _ ___| |_ _ __ | | __
                 | __| '_ \\| | '_ \\ / _` / __| | | '_ \\| |/ /
                 | |_| | | | | | | | (_| \\__ \\ | | | | |   <\s
                  \\__|_| |_|_|_| |_|\\__, |___/_|_|_| |_|_|\\_\\
                                     __/ |                  \s
                                    |___/                   \s""");
    }
}