package com.wzkris.auth.httpclient.token.resp;

import com.wzkris.common.core.model.LoginUser;
import lombok.Getter;
import lombok.ToString;

import java.io.Serializable;
import java.util.Set;

@Getter
@ToString
public class LoginUserResp implements Serializable {

    static final String SUCCESS = "success";

    static final String TEMPORARILY_UNAVAILABLE = "temporarily_unavailable";

    static final String FALL_BACK = "fall_back";

    private boolean success = false;

    private String errorCode;

    private String description;

    private LoginUser loginUser;

    private Set<String> permissions;

    public LoginUserResp() {
    }

    public LoginUserResp(String errorCode, String description, LoginUser loginUser, Set<String> permissions) {
        this.errorCode = errorCode;
        this.description = description;
        this.loginUser = loginUser;
        this.permissions = permissions;
        this.success = SUCCESS.equals(errorCode);
    }

    static LoginUserResp resp(String errorCode, String description, LoginUser loginUser) {
        return new LoginUserResp(errorCode, description, loginUser, null);
    }

    static LoginUserResp resp(String errorCode, String description, LoginUser loginUser, Set<String> permissions) {
        return new LoginUserResp(errorCode, description, loginUser, permissions);
    }

    public static LoginUserResp ok(LoginUser loginUser) {
        return resp(SUCCESS, null, loginUser, null);
    }

    public static LoginUserResp ok(LoginUser loginUser, Set<String> permissions) {
        return resp(SUCCESS, null, loginUser, permissions);
    }

    public static LoginUserResp okAnonymous() {
        return resp(SUCCESS, null, null, null);
    }

    public static LoginUserResp error(String errorCode, String description) {
        return resp(errorCode, description, null, null);
    }

    public static LoginUserResp unavailable(String description) {
        return error(TEMPORARILY_UNAVAILABLE, description);
    }

    public static LoginUserResp fallback(String description) {
        return error(FALL_BACK, description);
    }

}
