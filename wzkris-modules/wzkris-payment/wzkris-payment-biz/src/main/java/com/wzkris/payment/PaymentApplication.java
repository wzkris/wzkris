package com.wzkris.payment;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.ApplicationPidFileWriter;
import org.springframework.boot.context.metrics.buffering.BufferingApplicationStartup;

/**
 * 支付网关
 *
 * @author wzkris
 */
@SpringBootApplication
public class PaymentApplication {

    public static void main(String[] args) {
        SpringApplication springApplication = new SpringApplication(PaymentApplication.class);
        springApplication.addListeners(new ApplicationPidFileWriter());
        springApplication.setApplicationStartup(new BufferingApplicationStartup(2048));
        springApplication.run(args);
        System.out.println(
                """
                        
                          (♥◠‿◠)ﾉﾞ  支付网关启动成功   ლ(´ڡ`ლ)ﾞ
                        """);
    }

}
