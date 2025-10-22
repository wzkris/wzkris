package com.wzkris.principal;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.ApplicationPidFileWriter;
import org.springframework.boot.context.metrics.buffering.BufferingApplicationStartup;

/**
 * 主体信息中心
 *
 * @author wzkris
 */
@SpringBootApplication
public class PrincipalServerApplication {

    public static void main(String[] args) {
        SpringApplication springApplication = new SpringApplication(PrincipalServerApplication.class);
        springApplication.addListeners(new ApplicationPidFileWriter());
        springApplication.setApplicationStartup(new BufferingApplicationStartup(2048));
        springApplication.run(args);
        System.out.println(
                """
                        (♥◠‿◠)ﾉﾞ  用户中心启动成功   ლ(´ڡ`ლ)ﾞ
                          ________  ________  ___  ________   ________  ___  ________  ________  ___         \s
                         |\\   __  \\|\\   __  \\|\\  \\|\\   ___  \\|\\   ____\\|\\  \\|\\   __  \\|\\   __  \\|\\  \\        \s
                         \\ \\  \\|\\  \\ \\  \\|\\  \\ \\  \\ \\  \\\\ \\  \\ \\  \\___|\\ \\  \\ \\  \\|\\  \\ \\  \\|\\  \\ \\  \\       \s
                          \\ \\   ____\\ \\   _  _\\ \\  \\ \\  \\\\ \\  \\ \\  \\    \\ \\  \\ \\   ____\\ \\   __  \\ \\  \\      \s
                           \\ \\  \\___|\\ \\  \\\\  \\\\ \\  \\ \\  \\\\ \\  \\ \\  \\____\\ \\  \\ \\  \\___|\\ \\  \\ \\  \\ \\  \\____ \s
                            \\ \\__\\    \\ \\__\\\\ _\\\\ \\__\\ \\__\\\\ \\__\\ \\_______\\ \\__\\ \\__\\    \\ \\__\\ \\__\\ \\_______\\
                             \\|__|     \\|__|\\|__|\\|__|\\|__| \\|__|\\|_______|\\|__|\\|__|     \\|__|\\|__|\\|_______|
                         """);
    }

}
