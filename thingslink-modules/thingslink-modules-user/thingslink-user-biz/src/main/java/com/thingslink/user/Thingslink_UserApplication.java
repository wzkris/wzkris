package com.thingslink.user;

import com.thingslink.common.web.annotation.EnableCustomConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 认证授权中心
 *
 * @author wzkris
 */
@EnableCustomConfig
@SpringBootApplication
public class Thingslink_UserApplication {
    public static void main(String[] args) {
        SpringApplication.run(Thingslink_UserApplication.class, args);
        System.out.println("""
                (♥◠‿◠)ﾉﾞ  用户中心启动成功   ლ(´ڡ`ლ)ﾞ
                  _   _     _                 _ _       _
                 | | | |   (_)               | (_)     | |
                 | |_| |__  _ _ __   __ _ ___| |_ _ __ | | __
                 | __| '_ \\| | '_ \\ / _` / __| | | '_ \\| |/ /
                 | |_| | | | | | | | (_| \\__ \\ | | | | |   <
                  \\__|_| |_|_|_| |_|\\__, |___/_|_|_| |_|_|\\_\\
                                     __/ |       
                                    |___/                   """);
    }
}
