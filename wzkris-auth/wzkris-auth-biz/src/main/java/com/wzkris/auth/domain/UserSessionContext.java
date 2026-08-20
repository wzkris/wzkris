package com.wzkris.auth.domain;

public record UserSessionContext(boolean revoked, UserContext userContext) {

}