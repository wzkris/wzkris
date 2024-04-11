package com.thingslink.common.third.dingtalk.domain;

import lombok.Data;

/**
 * @author : wzkris
 * @version : V1.0.0
 * @description : 企业内部应用access_token
 * @date : 2023/5/22 8:03
 */
@Data
public class AccessToken {
    /**
     * 生成的accessToken。
     */
    private String accessToken;
    /**
     * accessToken的过期时间，单位秒。
     */
    private Long expireIn;
}
