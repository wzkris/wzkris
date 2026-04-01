package com.wzkris.captcha;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.ApplicationPidFileWriter;
import org.springframework.boot.context.metrics.buffering.BufferingApplicationStartup;

/**
 * 验证码中心
 */
@SpringBootApplication
public class CaptchaServerApplication {

    public static void main(String[] args) {
        SpringApplication springApplication = new SpringApplication(CaptchaServerApplication.class);
        springApplication.addListeners(new ApplicationPidFileWriter());
        springApplication.setApplicationStartup(new BufferingApplicationStartup(2048));
        springApplication.run(args);
    }

}
