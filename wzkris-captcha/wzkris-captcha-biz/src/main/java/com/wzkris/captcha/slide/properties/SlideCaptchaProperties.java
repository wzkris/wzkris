package com.wzkris.captcha.slide.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@ConfigurationProperties("captcha-slide")
@Configuration
public class SlideCaptchaProperties {

    private String captchaPrefix = "captcha-slide:";

    private String tokenPrefix = "captcha-slide-token:";

    private long captchaExpiresMs = 90_000L;

    private long tokenExpiresMs = 120_000L;

    private int tolerance = 5;

    private int idSize = 16;

}