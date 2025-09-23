package com.wzkris.user.feign.customer.req;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * 三方登录参数
 */
@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class SocialLoginReq {

    private String identifier;

    private String identifierType;

    // 其余客户字段

}
