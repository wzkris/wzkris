package com.wzkris.auth.remote.interfaces.tenantuser.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * 多渠道社交标识查询请求
 */
@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class SocialQueryRequest {

    private String socialType;

    private String wxCode;

    private String appid;

}