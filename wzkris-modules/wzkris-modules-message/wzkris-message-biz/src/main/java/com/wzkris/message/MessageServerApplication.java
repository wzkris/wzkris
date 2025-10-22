package com.wzkris.message;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.ApplicationPidFileWriter;
import org.springframework.boot.context.metrics.buffering.BufferingApplicationStartup;

/**
 * 消息模块
 *
 * @author wzkris
 */
@SpringBootApplication
public class MessageServerApplication {

    public static void main(String[] args) {
        SpringApplication springApplication = new SpringApplication(MessageServerApplication.class);
        springApplication.addListeners(new ApplicationPidFileWriter());
        springApplication.setApplicationStartup(new BufferingApplicationStartup(2048));
        springApplication.run(args);
        System.out.println(
                """
                        (♥◠‿◠)ﾉﾞ  消息模块启动成功   ლ(´ڡ`ლ)ﾞ \s
                         _____ ______   _______   ________   ________  ________  ________  _______     \s
                        |\\   _ \\  _   \\|\\  ___ \\ |\\   ____\\ |\\   ____\\|\\   __  \\|\\   ____\\|\\  ___ \\    \s
                        \\ \\  \\\\\\__\\ \\  \\ \\   __/|\\ \\  \\___|_\\ \\  \\___|\\ \\  \\|\\  \\ \\  \\___|\\ \\   __/|   \s
                         \\ \\  \\\\|__| \\  \\ \\  \\_|/_\\ \\_____  \\\\ \\_____  \\ \\   __  \\ \\  \\  __\\ \\  \\_|/__ \s
                          \\ \\  \\    \\ \\  \\ \\  \\_|\\ \\|____|\\  \\\\|____|\\  \\ \\  \\ \\  \\ \\  \\|\\  \\ \\  \\_|\\ \\\s
                           \\ \\__\\    \\ \\__\\ \\_______\\____\\_\\  \\ ____\\_\\  \\ \\__\\ \\__\\ \\_______\\ \\_______\\
                            \\|__|     \\|__|\\|_______|\\_________\\\\_________\\|__|\\|__|\\|_______|\\|_______|
                                                    \\|_________\\|_________|                            \s
                        """);
    }

}
