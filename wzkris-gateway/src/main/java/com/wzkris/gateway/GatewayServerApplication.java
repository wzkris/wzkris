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
        System.out.println(
                """
                (♥◠‿◠)ﾉﾞ  网关启动成功   ლ(´ڡ`ლ)ﾞ
                 ________  ________  _________  _______   ___       __   ________      ___    ___\s
                |\\   ____\\|\\   __  \\|\\___   ___\\\\  ___ \\ |\\  \\     |\\  \\|\\   __  \\    |\\  \\  /  /|
                \\ \\  \\___|\\ \\  \\|\\  \\|___ \\  \\_\\ \\   __/|\\ \\  \\    \\ \\  \\ \\  \\|\\  \\   \\ \\  \\/  / /
                 \\ \\  \\  __\\ \\   __  \\   \\ \\  \\ \\ \\  \\_|/_\\ \\  \\  __\\ \\  \\ \\   __  \\   \\ \\    / /\s
                  \\ \\  \\|\\  \\ \\  \\ \\  \\   \\ \\  \\ \\ \\  \\_|\\ \\ \\  \\|\\__\\_\\  \\ \\  \\ \\  \\   \\/  /  / \s
                   \\ \\_______\\ \\__\\ \\__\\   \\ \\__\\ \\ \\_______\\ \\____________\\ \\__\\ \\__\\__/  / /   \s
                    \\|_______|\\|__|\\|__|    \\|__|  \\|_______|\\|____________|\\|__|\\|__|\\___/ /    \s
                                                                                     \\|___|/     \s""");
    }
}
