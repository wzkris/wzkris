package com.wzkris.auth.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * Token Redis Lua 脚本枚举
 * <p>
 * 统一管理所有 Token 相关的 Lua 脚本，提高代码可维护性。
 * 所有脚本都使用相同的 hash tag ({type:uid})，确保在 Redis 集群模式下避免 CROSSSLOT 错误。
 * </p>
 *
 * @author wzkris
 */
@Getter
@RequiredArgsConstructor
public enum TokenLuaScriptsEnums {

    /**
     * 保存 Token 和用户信息（原子化操作）
     * <p>
     * 参数说明：
     * <ul>
     *   <li>KEYS[1] = userInfoKey (格式: auth-token:info:{type:uid})</li>
     *   <li>KEYS[2] = sessionKey (格式: auth-token:session:{type:uid})</li>
     *   <li>ARGV[1] = refreshToken</li>
     *   <li>ARGV[2] = userInfo (JSON 序列化的 LoginUser 对象)</li>
     *   <li>ARGV[3] = permissions (JSON 序列化的权限集合)</li>
     *   <li>ARGV[4] = sessionInfo (JSON 序列化的 OnlineSession 对象)</li>
     *   <li>ARGV[5] = refreshTTL (秒)</li>
     * </ul>
     * </p>
     * <p>
     * 返回值：1 表示成功，0 表示失败
     * </p>
     */
    SAVE_TOKEN_AND_USER_INFO("""
            -- 1. 设置用户信息 Hash
            redis.call('HSET', KEYS[1], 'loginUser', ARGV[2], 'permissions', ARGV[3])
            redis.call('EXPIRE', KEYS[1], ARGV[5])
                        
            -- 2. 设置在线会话（如果 sessionInfo 不为空）
            if ARGV[4] ~= '' then
                redis.call('HSET', KEYS[2], ARGV[1], ARGV[4])
                redis.call('EXPIRE', KEYS[2], ARGV[5])
            end
                        
            return 1
            """),

    /**
     * 登出操作（原子化删除）
     * <p>
     * 参数说明：
     * <ul>
     *   <li>KEYS[1] = userInfoKey (格式: auth-token:info:{type:uid})</li>
     *   <li>KEYS[2] = sessionKey (格式: auth-token:session:{type:uid})</li>
     *   <li>ARGV[1] = refreshToken</li>
     * </ul>
     * </p>
     * <p>
     * 返回值：1 表示成功，0 表示失败
     * </p>
     */
    LOGOUT_BY_REFRESH_TOKEN("""
            -- 1. 从会话中删除该 refreshToken
            redis.call('HDEL', KEYS[2], ARGV[1])
                        
            -- 2. 检查会话是否为空，如果为空则删除用户信息和会话
            local sessionSize = redis.call('HLEN', KEYS[2])
            if sessionSize == 0 then
                redis.call('DEL', KEYS[1])
                redis.call('DEL', KEYS[2])
            end
                        
            return 1
            """);

    /**
     * Lua 脚本内容
     */
    private final String script;

}
