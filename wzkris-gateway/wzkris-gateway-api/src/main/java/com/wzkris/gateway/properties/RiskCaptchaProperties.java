package com.wzkris.gateway.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * 网关风控验证码：全站锁 / 单路径强制 / 放行名单。
 */
@Data
@Component
@RefreshScope
@ConfigurationProperties(prefix = "security.risk-captcha")
public class RiskCaptchaProperties {

    private boolean enabled = true;

    /**
     * 前端换票后携带的通行票请求头（与 {@code captcha.risk-pass} 换票响应一致）。
     */
    private String passHeader = "X-Risk-Pass";

    /**
     * 未锁定且非强制路径时，是否沿用 {@link PermitUrlProperties#getIgnores()} 白名单直接放行。
     */
    private boolean permitIgnoresWhenIdle = true;

    /**
     * 无论是否加锁，始终允许访问（换票、验证码签发/换证等须在此配置真实网关路径）。
     * <p>注意：需要将验证码的网关路径放进此列表；不要让「仍需强制验证码的业务路径」（如登录）出现在此列表，
     * 否则会在下面 {@link #enforcedPaths} 之前就放行。
     */
    private List<String> exemptPaths = new ArrayList<>(List.of(
            "/actuator/**",
            "/health/**",
            "/doc/**",
            "/swagger-ui/**",
            "/v3/api-docs/**",
            "/webjars/**",
            "/**/captcha",
            "/**/captcha/**",
            "/**/risk-pass/**",
            "/**/captcha-remote/**",
            "/**/captcha-image-remote/**",
            "/**/captcha-slide-remote/**",
            "/**/captcha-challenge-remote/**"));

    /**
     * 每次请求都必须携带有效通行票的路径（如登录换 token），Ant 风格。
     */
    private List<String> enforcedPaths = new ArrayList<>();
}
