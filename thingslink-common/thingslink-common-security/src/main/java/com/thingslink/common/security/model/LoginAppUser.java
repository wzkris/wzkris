package com.thingslink.common.security.model;

import jakarta.annotation.Nonnull;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serial;


/**
 * @author : wzkris
 * @version : V1.0.1
 * @description : 登录顾客信息
 * @date : 2023/8/7 16:38
 * @UPDATE： 2024/4/9 09:29
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
public class LoginAppUser extends LoginUser {
    @Serial
    private static final long serialVersionUID = 4344390570373928224L;

    public static final String USER_TYPE = "app_user";
    // 登录id
    private Long userId;
    // 手机号
    private String phoneNumber;

    @Override
    public String getUserType() {
        return USER_TYPE;
    }

    @Override
    public String getPassword() {
        return null;
    }

    @Override
    @Nonnull
    public String getUsername() {
        return this.phoneNumber;
    }
}