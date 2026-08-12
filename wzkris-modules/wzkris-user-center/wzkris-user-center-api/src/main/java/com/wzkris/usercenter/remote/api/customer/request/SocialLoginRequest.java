package com.wzkris.usercenter.remote.api.customer.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

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
