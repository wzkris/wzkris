package com.demo.mq;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.ApplicationPidFileWriter;
import org.springframework.boot.context.metrics.buffering.BufferingApplicationStartup;

@SpringBootApplication
public class DemoMQApplication {

    public static void main(String[] args) {
        SpringApplication springApplication = new SpringApplication(DemoMQApplication.class);
        springApplication.addListeners(new ApplicationPidFileWriter());
        springApplication.setApplicationStartup(new BufferingApplicationStartup(2048));
        springApplication.run(args);
        System.out.println(
                """
                        (♥◠‿◠)ﾉﾞ  启动成功   ლ(´ڡ`ლ)ﾞ
                         """);
    }

}
