package com.wzkris.user;

import com.wzkris.common.web.annotation.EnableCustomConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.ApplicationPidFileWriter;
import org.springframework.boot.context.metrics.buffering.BufferingApplicationStartup;

/**
 * 认证授权中心
 *
 * @author wzkris
 */
@EnableCustomConfig
@SpringBootApplication
public class UserServerApplication {

    public static void main(String[] args) {
        SpringApplication springApplication = new SpringApplication(UserServerApplication.class);
        springApplication.addListeners(new ApplicationPidFileWriter());
        springApplication.setApplicationStartup(new BufferingApplicationStartup(2048));
        springApplication.run(args);
        System.out.println(
                """
                        (♥◠‿◠)ﾉﾞ  用户中心启动成功   ლ(´ڡ`ლ)ﾞ
                         ___  ___  ________  _______   ________    \s
                        |\\  \\|\\  \\|\\   ____\\|\\  ___ \\ |\\   __  \\   \s
                        \\ \\  \\\\\\  \\ \\  \\___|\\ \\   __/|\\ \\  \\|\\  \\  \s
                         \\ \\  \\\\\\  \\ \\_____  \\ \\  \\_|/_\\ \\   _  _\\ \s
                          \\ \\  \\\\\\  \\|____|\\  \\ \\  \\_|\\ \\ \\  \\\\  \\|\s
                           \\ \\_______\\____\\_\\  \\ \\_______\\ \\__\\\\ _\\\s
                            \\|_______|\\_________\\|_______|\\|__|\\|__|
                                     \\|_________|""");
    }

}
