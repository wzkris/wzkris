package com.wzkris.auth.service;

import com.wzkris.auth.domain.OnlineSession;
import com.wzkris.auth.domain.TokenPair;
import com.wzkris.common.core.model.DefaultLoginUser;
import com.wzkris.common.core.model.RoleContext;
import com.wzkris.common.core.support.LoginUser;
import jakarta.annotation.Nullable;

import java.io.Serializable;
import java.util.Map;

public interface TokenService {

    TokenPair loginCreate(LoginUser loginUser, RoleContext roleContext);

    TokenPair loginReuse(LoginUser loginUser, RoleContext roleContext, String sid);

    TokenPair loginRefresh(LoginUser loginUser, RoleContext roleContext, String refreshToken);

    @Nullable
    DefaultLoginUser loadLoginUserByUid(String type, Serializable uid);

    @Nullable
    RoleContext loadRoleContextByUid(String type, Serializable uid);

    Map<String, OnlineSession> loadSessionCache(String type, Serializable uid);

    void revoke(String type, Serializable uid, String sid);

    boolean isRevoked(String type, Long uid, String sid);

    default void revoke(LoginUser loginUser, String sid) {
        revoke(loginUser.getAuthType().getValue(), loginUser.getUid(), sid);
    }

}
