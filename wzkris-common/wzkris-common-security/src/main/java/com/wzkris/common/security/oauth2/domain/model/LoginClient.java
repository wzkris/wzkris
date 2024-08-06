package com.wzkris.common.security.oauth2.domain.model;

import com.wzkris.common.security.oauth2.domain.Loginer;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author : wzkris
 * @version : V1.0.1
 * @description : 登录客户端
 * @date : 2024/5/16 15:36
 */
@Data
@Accessors(chain = true)
public class LoginClient implements Loginer {

    private String clientId;

    private String clientName;
}
