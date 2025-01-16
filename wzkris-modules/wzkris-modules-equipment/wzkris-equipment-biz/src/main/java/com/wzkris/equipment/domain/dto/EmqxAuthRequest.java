package com.wzkris.equipment.domain.dto;

import lombok.Data;

/**
 * @author : wzkris
 * @version : V1.0.1
 * @description : emqx认证请求体
 * @date : 2024/4/13 14:13
 */
@Data
public class EmqxAuthRequest {

    private String clientid;

    private String username;

    private String password;
}
