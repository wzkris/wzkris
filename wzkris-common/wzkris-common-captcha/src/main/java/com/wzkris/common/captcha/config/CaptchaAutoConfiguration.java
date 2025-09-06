package com.wzkris.common.captcha.config;

import com.wzkris.common.captcha.handler.CapHandler;
import com.wzkris.common.captcha.properties.CapProperties;
import com.wzkris.common.captcha.service.CaptchaService;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.context.annotation.Import;

/**
 * 自动配置类
 */
@Import({CapProperties.class, CaptchaService.class, CapHandler.class})
@AutoConfiguration
public class CaptchaAutoConfiguration {

}
