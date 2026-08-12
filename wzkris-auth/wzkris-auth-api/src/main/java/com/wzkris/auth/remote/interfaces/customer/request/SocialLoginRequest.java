package com.wzkris.auth.remote.interfaces.customer.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * 多渠道社交登录参数
 */
@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class SocialLoginRequest {

    private String socialType;

    private String wxCode;

    private String phoneCode;

    private String appid;

}

