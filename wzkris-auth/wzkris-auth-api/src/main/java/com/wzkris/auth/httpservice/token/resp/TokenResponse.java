package com.wzkris.auth.httpservice.token.resp;

import com.wzkris.common.core.model.UserPrincipal;
import lombok.Getter;
import lombok.ToString;

import java.io.Serializable;

/**
 * @author : wzkris
 * @version : V1.0.0
 * @description :  token响应体
 * @date : 2025/01/08 14:55
 */
@Getter
@ToString
public class TokenResponse<T extends UserPrincipal> implements Serializable {

    static final String SUCCESS = "success";

    static final String TEMPORARILY_UNAVAILABLE = "temporarily_unavailable";

    static final String FALL_BACK = "fall_back";

    private boolean success = false;

    private String errorCode;

    private String description;

    private T principal;

    public TokenResponse() {
    }

    public TokenResponse(String errorCode, String description, T principal) {
        this.errorCode = errorCode;
        this.description = description;
        this.principal = principal;
        this.success = SUCCESS.equals(errorCode);
    }

    static <T extends UserPrincipal> TokenResponse<T> resp(String errorCode, String description, T principal) {
        return new TokenResponse<>(errorCode, description, principal);
    }

    public static <T extends UserPrincipal> TokenResponse<T> ok(T principal) {
        return resp(SUCCESS, null, principal);
    }

    public static <T extends UserPrincipal> TokenResponse<T> okAnonymous() {
        return resp(SUCCESS, null, null);
    }

    public static <T extends UserPrincipal> TokenResponse<T> error(String errorCode, String description) {
        return resp(errorCode, description, null);
    }

    public static <T extends UserPrincipal> TokenResponse<T> unavailable(String description) {
        return error(TEMPORARILY_UNAVAILABLE, description);
    }

    public static <T extends UserPrincipal> TokenResponse<T> fallback(String description) {
        return error(FALL_BACK, description);
    }

}
