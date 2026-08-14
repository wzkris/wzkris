package com.wzkris.auth.service;

import com.wzkris.auth.domain.OnlineSession;
import com.wzkris.auth.domain.TokenPair;
import com.wzkris.auth.domain.UserContext;
import com.wzkris.auth.domain.UserSessionContext;
import com.wzkris.common.core.model.RoleContext;
import com.wzkris.common.core.support.LoginUser;

import java.io.Serializable;
import java.util.Map;

public interface TokenService {

    TokenPair loginCreate(LoginUser loginUser, RoleContext roleContext);

    TokenPair loginReuse(LoginUser loginUser, RoleContext roleContext, Serializable sid);

    TokenPair loginRefresh(LoginUser loginUser, RoleContext roleContext, String refreshToken);

    UserContext loadUserContext(String type, Serializable uid);

    UserSessionContext loadUserSessionContext(String type, Serializable uid, Serializable sid);

    Map<String, OnlineSession> loadSessionCache(String type, Serializable uid);

    void revoke(String type, Serializable uid, Serializable sid);

}
