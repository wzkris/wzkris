package com.wzkris.auth.httpservice.token.resp;

import com.wzkris.common.core.model.LoginUser;
import lombok.Getter;
import lombok.ToString;

import java.io.Serializable;
import java.util.Set;

/**
 * @author : wzkris
 * @version : V1.0.0
 * @description :  token响应体
 * @date : 2025/01/08 14:55
 */
@Getter
@ToString
public class TokenResponse implements Serializable {

    static final String SUCCESS = "success";

    static final String TEMPORARILY_UNAVAILABLE = "temporarily_unavailable";

    static final String FALL_BACK = "fall_back";

    private boolean success = false;

    private String errorCode;

    private String description;

    private LoginUser loginUser;

    private Set<String> permissions;

    public TokenResponse() {
    }

    public TokenResponse(String errorCode, String description, LoginUser loginUser, Set<String> permissions) {
        this.errorCode = errorCode;
        this.description = description;
        this.loginUser = loginUser;
        this.permissions = permissions;
        this.success = SUCCESS.equals(errorCode);
    }

    static TokenResponse resp(String errorCode, String description, LoginUser loginUser) {
        return new TokenResponse(errorCode, description, loginUser, null);
    }

    static TokenResponse resp(String errorCode, String description, LoginUser loginUser, Set<String> permissions) {
        return new TokenResponse(errorCode, description, loginUser, permissions);
    }

    public static TokenResponse ok(LoginUser loginUser) {
        return resp(SUCCESS, null, loginUser, null);
    }

    public static TokenResponse ok(LoginUser loginUser, Set<String> permissions) {
        return resp(SUCCESS, null, loginUser, permissions);
    }

    public static TokenResponse okAnonymous() {
        return resp(SUCCESS, null, null, null);
    }

    public static TokenResponse error(String errorCode, String description) {
        return resp(errorCode, description, null, null);
    }

    public static TokenResponse unavailable(String description) {
        return error(TEMPORARILY_UNAVAILABLE, description);
    }

    public static TokenResponse fallback(String description) {
        return error(FALL_BACK, description);
    }

}
