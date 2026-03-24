package com.wzkris.captcha.image.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@ConfigurationProperties("captcha-image")
@Configuration
public class ImageCaptchaProperties {

    private String captchaPrefix = "captcha-image:";

    private String tokenPrefix = "captcha-image-token:";

    private long captchaExpiresMs = 90_000L;

    private long tokenExpiresMs = 120_000L;

    private int width = 120;

    private int height = 40;

    private int codeLength = 4;

    private int idSize = 16;

}
