package com.wzkris.principal.utils;

import com.wzkris.common.core.model.domain.LoginUser;
import com.wzkris.common.redis.util.RedisUtil;
import com.wzkris.principal.PrincipalServerApplication;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.HashSet;

@SpringBootTest(classes = PrincipalServerApplication.class)
public class RedisUtilTest {

    @Test
    public void test1() {
        RedisUtil.setObj("1", new LoginUser(1L, new HashSet<>(), true, "admin", null), 100);

        LoginUser loginUser = RedisUtil.getObj("1", LoginUser.class);
        System.out.println(loginUser);
    }

}
