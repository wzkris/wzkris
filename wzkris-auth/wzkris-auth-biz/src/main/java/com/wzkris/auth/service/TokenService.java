package com.wzkris.auth.service;

import com.wzkris.auth.domain.OnlineSession;
import com.wzkris.auth.domain.TokenClaims;
import com.wzkris.common.core.model.BaseLoginUser;
import jakarta.annotation.Nullable;

import java.io.Serializable;
import java.util.Map;
import java.util.Set;

public interface TokenService {

    @Nullable
    String generateAccessToken(BaseLoginUser baseLoginUser, String sid);

    @Nullable
    String generateRefreshToken(BaseLoginUser baseLoginUser, String sid);

    void save(BaseLoginUser baseLoginUser, String sid, Set<String> permissions);

    @Nullable
    BaseLoginUser loadLoginUserByUid(String type, Serializable uid);

    @Nullable
    Set<String> loadPermissionsByUid(String type, Serializable uid);

    void revoke(String type, Serializable uid, String sid);

    boolean isRevoked(String type, Long uid, String sid);

    Map<String, OnlineSession> loadSessionCache(String type, Serializable uid);

    TokenClaims parseJwt(String token);

}
