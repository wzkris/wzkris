package com.wzkris.monitor;

import de.codecentric.boot.admin.server.config.EnableAdminServer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.ApplicationPidFileWriter;
import org.springframework.boot.context.metrics.buffering.BufferingApplicationStartup;

/**
 * 监控中心
 *
 * @author wzkris
 */
@EnableAdminServer
@SpringBootApplication
public class MonitorServerApplication {

    public static void main(String[] args) {
        SpringApplication springApplication = new SpringApplication(MonitorServerApplication.class);
        springApplication.addListeners(new ApplicationPidFileWriter());
        springApplication.setApplicationStartup(new BufferingApplicationStartup(2048));
        springApplication.run(args);
        System.out.println("""
                (♥◠‿◠)ﾉﾞ  监控中心启动成功   ლ(´ڡ`ლ)ﾞ \s
                 _____ ______   ________  ________   ___  _________  ________  ________    \s
                |\\   _ \\  _   \\|\\   __  \\|\\   ___  \\|\\  \\|\\___   ___\\\\   __  \\|\\   __  \\   \s
                \\ \\  \\\\\\__\\ \\  \\ \\  \\|\\  \\ \\  \\\\ \\  \\ \\  \\|___ \\  \\_\\ \\  \\|\\  \\ \\  \\|\\  \\  \s
                 \\ \\  \\\\|__| \\  \\ \\  \\\\\\  \\ \\  \\\\ \\  \\ \\  \\   \\ \\  \\ \\ \\  \\\\\\  \\ \\   _  _\\ \s
                  \\ \\  \\    \\ \\  \\ \\  \\\\\\  \\ \\  \\\\ \\  \\ \\  \\   \\ \\  \\ \\ \\  \\\\\\  \\ \\  \\\\  \\|\s
                   \\ \\__\\    \\ \\__\\ \\_______\\ \\__\\\\ \\__\\ \\__\\   \\ \\__\\ \\ \\_______\\ \\__\\\\ _\\\s
                    \\|__|     \\|__|\\|_______|\\|__| \\|__|\\|__|    \\|__|  \\|_______|\\|__|\\|__|
                                                                                          \s""");
    }
}
