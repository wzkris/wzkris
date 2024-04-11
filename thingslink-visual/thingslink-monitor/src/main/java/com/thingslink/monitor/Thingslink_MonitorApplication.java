package com.thingslink.monitor;

import de.codecentric.boot.admin.server.config.EnableAdminServer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 监控中心
 *
 * @author wzkris
 */
@EnableAdminServer
@SpringBootApplication
public class Thingslink_MonitorApplication {
    public static void main(String[] args) {
        SpringApplication.run(com.thingslink.monitor.Thingslink_MonitorApplication.class, args);
        System.out.println("(♥◠‿◠)ﾉﾞ  监控中心启动成功   ლ(´ڡ`ლ)ﾞ  \n" +
                "  __  .__    .__                     .__  .__        __    \n" +
                "_/  |_|  |__ |__| ____    ____  _____|  | |__| ____ |  | __\n" +
                "\\   __\\  |  \\|  |/    \\  / ___\\/  ___/  | |  |/    \\|  |/ /\n" +
                " |  | |   Y  \\  |   |  \\/ /_/  >___ \\|  |_|  |   |  \\    < \n" +
                " |__| |___|  /__|___|  /\\___  /____  >____/__|___|  /__|_ \\\n" +
                "           \\/        \\//_____/     \\/             \\/     \\/");
    }
}
