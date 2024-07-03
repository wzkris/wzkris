package com.wzkris.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.ApplicationPidFileWriter;
import org.springframework.boot.context.metrics.buffering.BufferingApplicationStartup;

/**
 * 网关启动程序
 *
 * @author wzkris
 */
@SpringBootApplication
public class GatewayServerApplication {

    public static void main(String[] args) {
        SpringApplication springApplication = new SpringApplication(GatewayServerApplication.class);
        springApplication.addListeners(new ApplicationPidFileWriter());
        springApplication.setApplicationStartup(new BufferingApplicationStartup(2048));
        springApplication.run(args);
        System.out.println("""
                (♥◠‿◠)ﾉﾞ  网关启动成功   ლ(´ڡ`ლ)ﾞ
                  _   _     _                 _ _       _
                 | | | |   (_)               | (_)     | |
                 | |_| |__  _ _ __   __ _ ___| |_ _ __ | | __
                 | __| '_ \\| | '_ \\ / _` / __| | | '_ \\| |/ /
                 | |_| | | | | | | | (_| \\__ \\ | | | | |   <
                  \\__|_| |_|_|_| |_|\\__, |___/_|_|_| |_|_|\\_\\
                                     __/ |
                                    |___/""");
    }
}
