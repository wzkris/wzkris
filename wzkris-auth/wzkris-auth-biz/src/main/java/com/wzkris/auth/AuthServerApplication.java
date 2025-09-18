package com.wzkris.auth;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.ApplicationPidFileWriter;
import org.springframework.boot.context.metrics.buffering.BufferingApplicationStartup;

/**
 * 认证授权中心
 *
 * @author wzkris
 */
@SpringBootApplication
public class AuthServerApplication {

    public static void main(String[] args) {
        SpringApplication springApplication = new SpringApplication(AuthServerApplication.class);
        springApplication.addListeners(new ApplicationPidFileWriter());
        springApplication.setApplicationStartup(new BufferingApplicationStartup(2048));
        springApplication.run(args);
        System.out.println(
                """
                        (♥◠‿◠)ﾉﾞ  认证授权中心启动成功   ლ(´ڡ`ლ)ﾞ
                         ________  ___  ___  _________  ___  ___    \s
                        |\\   __  \\|\\  \\|\\  \\|\\___   ___\\\\  \\|\\  \\   \s
                        \\ \\  \\|\\  \\ \\  \\\\\\  \\|___ \\  \\_\\ \\  \\\\\\  \\  \s
                         \\ \\   __  \\ \\  \\\\\\  \\   \\ \\  \\ \\ \\   __  \\ \s
                          \\ \\  \\ \\  \\ \\  \\\\\\  \\   \\ \\  \\ \\ \\  \\ \\  \\\s
                           \\ \\__\\ \\__\\ \\_______\\   \\ \\__\\ \\ \\__\\ \\__\\
                            \\|__|\\|__|\\|_______|    \\|__|  \\|__|\\|__|""");
    }

}
