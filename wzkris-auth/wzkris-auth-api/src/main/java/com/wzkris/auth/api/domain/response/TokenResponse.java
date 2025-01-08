package com.wzkris.auth.api.domain.response;

import lombok.Getter;
import lombok.ToString;

/**
 * @author : wzkris
 * @version : V1.0.0
 * @description :  token响应体
 * @date : 2025/01/08 14:55
 */
@Getter
@ToString
public class TokenResponse {

    private final boolean success;

    private String errorCode;

    private String description;

    private Object principal;

    public TokenResponse() {
        this.success = false;
    }

    public static final String SUCCESS = "success";

    public static final String TEMPORARILY_UNAVAILABLE = "temporarily_unavailable";

    public TokenResponse(String errorCode, String description, Object principal) {
        this.errorCode = errorCode;
        this.description = description;
        this.principal = principal;
        this.success = SUCCESS.equals(errorCode);
    }

    public static TokenResponse resp(String errorCode, String description, Object principal) {
        return new TokenResponse(errorCode, description, principal);
    }

    public static TokenResponse failed(String errorCode, String description) {
        return resp(errorCode, description, null);
    }

    public static TokenResponse ok(Object principal) {
        return resp(SUCCESS, null, principal);
    }

    public static TokenResponse unavailable(String description) {
        return failed(TEMPORARILY_UNAVAILABLE, description);
    }

}
