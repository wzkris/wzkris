package com.wzkris.common.security.oauth2.domain.model;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * @author : wzkris
 * @version : V1.0.1
 * @description : 登录客户端
 * @date : 2024/5/16 15:36
 */
@Data
@Accessors(chain = true)
public class LoginClient implements Serializable {

    private String clientId;
    private String clientName;
}
