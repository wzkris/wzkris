package com.thingslink.auth.oauth2.redis;

import com.thingslink.common.redis.util.RedisUtil;
import lombok.Getter;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.codec.SerializationCodec;
import org.redisson.config.Config;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

@Component
public class JdkRedisUtil implements InitializingBean {
    @Getter
    private static RedissonClient redissonClient;

    /**
     * 使用JDK序列化，否则OAuth2无法转为JAVA类
     */
    @Override
    public void afterPropertiesSet() throws Exception {
        Config config = RedisUtil.getClient().getConfig();
        config.setCodec(new SerializationCodec());
        redissonClient = Redisson.create(config);
    }

}
