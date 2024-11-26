package com.wzkris.common.security.oauth2.domain.model;

import lombok.Data;

import java.io.Serializable;


/**
 * @author : wzkris
 * @version : V1.0.1
 * @description : 登录顾客信息
 * @date : 2023/8/7 16:38
 * @UPDATE： 2024/4/9 09:29
 */
@Data
public class LoginApper implements Serializable {

    /**
     * 登录id
     */
    private Long userId;
    /**
     * 手机号
     */
    private String phoneNumber;

}
