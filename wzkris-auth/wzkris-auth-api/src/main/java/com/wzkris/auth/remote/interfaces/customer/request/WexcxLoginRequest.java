package com.wzkris.auth.remote.interfaces.customer.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * 微信小程序登录参数
 */
@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class WexcxLoginRequest {

    private String identifier;

    private String phoneNumber;

}

