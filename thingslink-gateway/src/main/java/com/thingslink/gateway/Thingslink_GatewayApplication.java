package com.thingslink.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 网关启动程序
 *
 * @author wzkris
 */
@SpringBootApplication
public class Thingslink_GatewayApplication {
    public static void main(String[] args) {
        SpringApplication.run(Thingslink_GatewayApplication.class, args);
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
