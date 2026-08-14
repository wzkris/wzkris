package com.wzkris.auth.utils;

import java.io.Serializable;

/**
 * Token Redis Key 构建工具类
 * <p>
 * 统一管理 Redis Key 构建逻辑，通过 hash tag 确保 Redis 集群模式下
 * 同一用户的相关数据（会话、用户信息）在同一 slot，支持原子操作。
 * </p>
 * <p>
 * Hash Tag 说明：
 * <ul>
 *   <li>使用 {type:uid} 作为 hash tag，确保同一用户的所有数据映射到同一 Redis slot</li>
 *   <li>支持在 Lua 脚本中对多个 key 进行原子操作，避免 CROSSSLOT 错误</li>
 * </ul>
 * </p>
 *
 * @author wzkris
 */
public class TokenKeyBuilder {

    /**
     * 在线会话 Key 前缀
     * <p>
     * 格式：auth-token:session:{type:uid}
     * </p>
     * <p>
     * Hash tag: {type:uid} 确保同一用户的所有会话数据在同一 slot
     * </p>
     */
    private static final String SESSION_INDEX_PREFIX = "auth-token:session:{%s:%s}";

    /**
     * 用户信息 Hash Key 前缀
     * <p>
     * 格式：auth-token:info:{type:uid}
     * </p>
     * <p>
     * Redis结构：Hash，包含loginUser和permissions字段
     * </p>
     * <p>
     * Hash tag: {type:uid} 确保用户信息与会话数据在同一 slot
     * </p>
     */
    private static final String USER_INFO_PREFIX = "auth-token:info:{%s:%s}";

    /**
     * 单个会话元数据 Key（每个 sid 一个 key），TTL=refreshTokenTimeOut，随会话到期自动消失。
     * 格式：auth-token:session:{type:uid}:sid
     * 使用 {type:uid} 作为 hash tag，保证与用户相关的 keys 在同一 slot
     */
    private static final String SESSION_ENTRY_PREFIX = "auth-token:session:{%s:%s}:%s";

    /**
     * 构建在线会话 Key
     * <p>
     * 返回格式：auth-token:session:{type:uid}
     * </p>
     * <p>
     * Redis结构：Hash，field为sid（会话ID），value为OnlineSession对象
     * </p>
     *
     * @param type 认证类型
     * @param uid  用户ID
     * @return Redis Key（使用 {type:uid} hash tag）
     */
    public static String buildSessionIndexKey(String type, Serializable uid) {
        return SESSION_INDEX_PREFIX.formatted(type, uid);
    }

    /**
     * 构建单个会话元数据 Key
     * 返回格式：auth-token:session:{type:uid}:sid
     *
     * @param type 认证类型
     * @param uid  用户ID
     * @param sid  会话ID
     * @return Redis Key（使用 {type:uid} hash tag）
     */
    public static String buildSessionEntryKey(String type, Serializable uid, Serializable sid) {
        return SESSION_ENTRY_PREFIX.formatted(type, uid, sid);
    }

    /**
     * 构建用户信息 Hash Key
     * <p>
     * 返回格式：auth-token:info:{type:uid}
     * </p>
     * <p>
     * Redis结构：Hash，包含loginUser和permissions字段
     * </p>
     *
     * @param type 认证类型
     * @param uid  用户ID
     * @return Redis Key（使用 {type:uid} hash tag）
     */
    public static String buildUserInfoKey(String type, Serializable uid) {
        return USER_INFO_PREFIX.formatted(type, uid);
    }

}
