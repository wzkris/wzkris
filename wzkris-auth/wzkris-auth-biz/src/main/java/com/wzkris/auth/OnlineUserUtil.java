package com.wzkris.auth;

import com.wzkris.auth.domain.OnlineUser;
import com.wzkris.common.redis.util.RedisUtil;
import org.redisson.api.RMapCache;

/**
 * 用户在线会话工具
 *
 * @author wzkris
 * @date 2025/04/18
 */
public class OnlineUserUtil {

    private static final String ONLINE_USER_KEY = "online:user:";

    public static RMapCache<String, OnlineUser> getOnlineCache(Long userId) {
        return RedisUtil.getRMapCache(ONLINE_USER_KEY + userId);
    }
}
