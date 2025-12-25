package com.wzkris.usercenter.mapper.utils;

import com.wzkris.common.core.model.domain.LoginAdmin;
import com.wzkris.common.redis.util.RedisUtil;
import com.wzkris.usercenter.UserCenterApplication;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Collections;

@SpringBootTest(classes = UserCenterApplication.class)
public class RedisUtilTest {

    @Test
    public void test1() {
        LoginAdmin loginAdmin = new LoginAdmin(1L, Collections.singleton("*"));
        loginAdmin.setSuperadmin(true);
        loginAdmin.setUsername("admin");
        loginAdmin.setDeptScopes(Collections.emptyList());
        RedisUtil.setObj("1", loginAdmin, 100);

        LoginAdmin loginAdmin1 = RedisUtil.getObj("1", LoginAdmin.class);
        System.out.println(loginAdmin1);
    }

}
