package com.thingslink.system;

import com.thingslink.common.web.annotation.EnableCustomConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 系统模块
 *
 * @author wzkris
 */
@EnableCustomConfig
@SpringBootApplication
public class Thingslink_SystemApplication {
    public static void main(String[] args) {
        SpringApplication.run(Thingslink_SystemApplication.class, args);
        System.out.println("""
                (♥◠‿◠)ﾉﾞ  系统模块启动成功   ლ(´ڡ`ლ)ﾞ \s
                  _   _     _                 _ _       _   \s
                 | | | |   (_)               | (_)     | |  \s
                 | |_| |__  _ _ __   __ _ ___| |_ _ __ | | __
                 | __| '_ \\| | '_ \\ / _` / __| | | '_ \\| |/ /
                 | |_| | | | | | | | (_| \\__ \\ | | | | |   <\s
                  \\__|_| |_|_|_| |_|\\__, |___/_|_|_| |_|_|\\_\\
                                     __/ |                  \s
                                    |___/                   \s""");
    }
}
