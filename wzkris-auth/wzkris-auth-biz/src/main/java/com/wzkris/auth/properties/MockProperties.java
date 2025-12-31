package com.wzkris.auth.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * Mock开关
 */
@Data
@Configuration
@ConfigurationProperties("mock")
public class MockProperties {

    private Boolean captchaValidateMock = false;

    private Boolean captchaMaxtryMock = false;

}
