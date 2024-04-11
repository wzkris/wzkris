package com.thingslink.auth.listening.event;

import com.thingslink.common.security.model.AbstractUser;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @author : wzkris
 * @version : V1.0.0
 * @description : 用户登录事件
 * @date : 2024/2/21 9:09
 */
@Data
@AllArgsConstructor
public class UserLoginEvent {
    private AbstractUser userInfo;
    private HttpServletRequest request;
}
