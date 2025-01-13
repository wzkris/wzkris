package com.wzkris.equipment;

import com.wzkris.common.web.annotation.EnableCustomConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.ApplicationPidFileWriter;
import org.springframework.boot.context.metrics.buffering.BufferingApplicationStartup;

/**
 * 系统模块
 *
 * @author wzkris
 */
@EnableCustomConfig
@SpringBootApplication
public class EquipmentServerApplication {

    public static void main(String[] args) {
        SpringApplication springApplication = new SpringApplication(EquipmentServerApplication.class);
        springApplication.addListeners(new ApplicationPidFileWriter());
        springApplication.setApplicationStartup(new BufferingApplicationStartup(2048));
        springApplication.run(args);
        System.out.println("""
                (♥◠‿◠)ﾉﾞ  设备模块启动成功   ლ(´ڡ`ლ)ﾞ
                 _______   ________  ___  ___  ___  ________  _____ ______   _______   ________   _________  \s
                |\\  ___ \\ |\\   __  \\|\\  \\|\\  \\|\\  \\|\\   __  \\|\\   _ \\  _   \\|\\  ___ \\ |\\   ___  \\|\\___   ___\\\s
                \\ \\   __/|\\ \\  \\|\\  \\ \\  \\\\\\  \\ \\  \\ \\  \\|\\  \\ \\  \\\\\\__\\ \\  \\ \\   __/|\\ \\  \\\\ \\  \\|___ \\  \\_|\s
                 \\ \\  \\_|/_\\ \\  \\\\\\  \\ \\  \\\\\\  \\ \\  \\ \\   ____\\ \\  \\\\|__| \\  \\ \\  \\_|/_\\ \\  \\\\ \\  \\   \\ \\  \\ \s
                  \\ \\  \\_|\\ \\ \\  \\\\\\  \\ \\  \\\\\\  \\ \\  \\ \\  \\___|\\ \\  \\    \\ \\  \\ \\  \\_|\\ \\ \\  \\\\ \\  \\   \\ \\  \\\s
                   \\ \\_______\\ \\_____  \\ \\_______\\ \\__\\ \\__\\    \\ \\__\\    \\ \\__\\ \\_______\\ \\__\\\\ \\__\\   \\ \\__\\
                    \\|_______|\\|___| \\__\\|_______|\\|__|\\|__|     \\|__|     \\|__|\\|_______|\\|__| \\|__|    \\|__|
                                    \\|__|                                                                    \s""");
    }
}
