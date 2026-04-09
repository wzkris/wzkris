package com.wzkris.auth.service;

import com.wzkris.auth.domain.OnlineSession;
import com.wzkris.auth.domain.TokenClaims;
import com.wzkris.auth.domain.TokenPair;
import com.wzkris.common.core.model.BaseLoginUser;
import jakarta.annotation.Nullable;

import java.io.Serializable;
import java.util.Map;
import java.util.Set;

public interface TokenService {

    /**
     * 首次登录：生成新 sid，签发 accessToken 和 refreshToken，保存会话。
     *
     * @param loginUser   用户信息
     * @param permissions 权限集合
     * @return 令牌对
     */
    TokenPair login(BaseLoginUser loginUser, Set<String> permissions);

    /**
     * 刷新令牌：根据旧 refreshToken 签发新的 accessToken（和可能的新 refreshToken），维护会话。
     *
     * @param loginUser       用户信息
     * @param permissions     权限集合
     * @param oldRefreshToken 旧的 refreshToken
     * @return 令牌对
     */
    TokenPair refresh(BaseLoginUser loginUser, Set<String> permissions, String oldRefreshToken);

    /**
     * 按 uid 获取用户信息。
     *
     * @param type 认证类型
     * @param uid  用户ID
     * @return 用户信息，不存在返回 null
     */
    @Nullable
    BaseLoginUser loadLoginUserByUid(String type, Serializable uid);

    /**
     * 按 uid 获取权限。
     *
     * @param type 认证类型
     * @param uid  用户ID
     * @return 权限集合，不存在返回 null
     */
    @Nullable
    Set<String> loadPermissionsByUid(String type, Serializable uid);

    /**
     * 按 sid 移除会话；若无其它会话则删除 userInfo/session key。
     *
     * @param type 认证类型
     * @param uid  用户ID
     * @param sid  会话ID
     */
    void revoke(String type, Serializable uid, String sid);

    /**
     * 检查 sid 是否已拉黑
     *
     * @param type 认证类型
     * @param uid  用户ID
     * @param sid  会话ID
     * @return 已拉黑为 true，否则 false
     */
    boolean isRevoked(String type, Long uid, String sid);

    /**
     * 按 uid 拉取在线会话 Map（sid -> OnlineSession）。
     *
     * @param type 认证类型
     * @param uid  用户ID
     * @return sid -> OnlineSession，无会话返回空 Map
     */
    Map<String, OnlineSession> loadSessionCache(String type, Serializable uid);

    TokenClaims parseJwt(String token);

}
