package com.wzkris.principal.httpservice.customer.req;

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
public class WexcxLoginReq {

    private String identifier;

    private String phoneNumber;

}
