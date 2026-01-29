package com.wzkris.auth.service;

import java.io.Serializable;

/**
 * Token Redis Key 构建工具类
 * <p>
 * 统一管理 Redis Key 构建逻辑，通过 hash tag 确保 Redis 集群模式下
 * 同一用户的相关数据（会话、用户信息）在同一 slot，支持原子操作。
 * </p>
 *
 * @author wzkris
 */
public class TokenKeyBuilder {

    /**
     * 在线会话 Key 前缀
     * 格式：auth-token:session:{type:uid}
     * hash tag: {type:uid} 确保同一用户的所有会话数据在同一 slot
     */
    private static final String SESSION_PREFIX = "auth-token:session:{%s:%s}";

    /**
     * Refresh Token 映射 Key 前缀
     * 格式：auth-token:refresh:type:refreshToken
     * 注意：refreshToken 是随机生成的，无法使用 hash tag 与用户信息关联
     */
    private static final String REFRESH_TOKEN_PREFIX = "auth-token:refresh:%s:%s";

    /**
     * 用户信息 Hash Key 前缀
     * 格式：auth-token:info:{type:uid}
     * hash tag: {type:uid} 确保用户信息与会话数据在同一 slot
     */
    private static final String USER_INFO_PREFIX = "auth-token:info:{%s:%s}";

    /**
     * 构建在线会话 Key
     *
     * @param type 认证类型
     * @param uid  用户ID
     * @return Redis Key
     */
    public static String buildSessionKey(String type, Serializable uid) {
        return SESSION_PREFIX.formatted(type, uid);
    }

    /**
     * 构建 Refresh Token 到 UID 的映射 Key
     * <p>
     * refreshToken 是随机生成的，无法使用 hash tag 与用户信息关联。
     * 通过 Lua 脚本先获取 uid，然后使用 uid 构建其他相关 key。
     * </p>
     *
     * @param type         认证类型
     * @param refreshToken Refresh Token
     * @return Redis Key
     */
    public static String buildRefreshTokenToUidKey(String type, String refreshToken) {
        return REFRESH_TOKEN_PREFIX.formatted(type, refreshToken);
    }

    /**
     * 构建用户信息 Hash Key
     *
     * @param type 认证类型
     * @param uid  用户ID
     * @return Redis Key
     */
    public static String buildUserInfoKey(String type, Serializable uid) {
        return USER_INFO_PREFIX.formatted(type, uid);
    }

}
