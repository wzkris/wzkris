package com.wzkris.usercenter.mapper.utils;

import com.wzkris.common.core.model.LoginUser;
import com.wzkris.common.core.enums.AuthTypeEnum;
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
        LoginUser loginUser = new LoginUser();
        loginUser.setUid(1L);
        loginUser.setAuthType(AuthTypeEnum.ADMIN);
        loginUser.setUsername("admin");
        redisTemplate.opsForValue().set("1", loginUser, Duration.ofSeconds(100));

        Object value = redisTemplate.opsForValue().get("1");
        LoginUser loginUser1 = value instanceof LoginUser ? (LoginUser) value : null;
        System.out.println(loginUser1);
    }

}
