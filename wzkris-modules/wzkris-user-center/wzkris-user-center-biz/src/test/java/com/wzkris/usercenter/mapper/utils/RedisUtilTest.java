package com.wzkris.usercenter.mapper.utils;

import com.wzkris.common.core.enums.AuthTypeEnum;
import com.wzkris.common.core.model.DefaultLoginUser;
import com.wzkris.common.redis.util.RedisJsonUtil;
import com.wzkris.usercenter.UserCenterApplication;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;

import java.time.Duration;

@SpringBootTest(classes = UserCenterApplication.class)
public class RedisUtilTest {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Test
    public void test1() {
        DefaultLoginUser loginUser = new DefaultLoginUser();
        loginUser.setUid(1L);
        loginUser.setAuthType(AuthTypeEnum.ADMIN);
        loginUser.setName("admin");
        redisTemplate.opsForValue().set("1", loginUser, Duration.ofSeconds(100));

        Object value = redisTemplate.opsForValue().get("1");
        DefaultLoginUser loginUser1 = RedisJsonUtil.parse(value, DefaultLoginUser.class);
        System.out.println(loginUser1);
    }

}
