package com.wzkris.auth.service;

import com.wzkris.auth.domain.OnlineSession;
import com.wzkris.auth.domain.TokenPair;
import com.wzkris.common.core.model.BaseLoginUser;
import jakarta.annotation.Nullable;

import java.io.Serializable;
import java.util.Map;
import java.util.Set;

/**
 * 登录令牌与会话编排：JWT 签发 + Redis 会话（userInfo / sid 索引 / OnlineSession）。
 */
public interface TokenService {

    // --- 签发 ---

    /**
     * 新建会话：生成 sid、写入 OnlineSession 元数据并签发令牌（登录、进入代操作）。
     */
    TokenPair loginCreate(BaseLoginUser loginUser, Set<String> permissions);

    /**
     * 复用已有 sid
     *
     * @param sid 须为有效且未 revoke 的会话 ID
     */
    TokenPair loginReuse(BaseLoginUser loginUser, Set<String> permissions, String sid);

    /**
     * 刷新令牌：按配置复用或轮换 sid，维护会话并签发新令牌对。
     */
    TokenPair loginRefresh(BaseLoginUser loginUser, Set<String> permissions, String refreshToken);

    // --- 查询 ---

    @Nullable
    BaseLoginUser loadLoginUserByUid(String type, Serializable uid);

    @Nullable
    Set<String> loadPermissionsByUid(String type, Serializable uid);

    Map<String, OnlineSession> loadSessionCache(String type, Serializable uid);

    // --- 生命周期 ---

    void revoke(String type, Serializable uid, String sid);

    /**
     * @return true 表示 sid 已失效（不存在或已移除）
     */
    boolean isRevoked(String type, Long uid, String sid);

    default void revoke(BaseLoginUser loginUser, String sid) {
        revoke(loginUser.getAuthType().getValue(), loginUser.getUid(), sid);
    }

}
