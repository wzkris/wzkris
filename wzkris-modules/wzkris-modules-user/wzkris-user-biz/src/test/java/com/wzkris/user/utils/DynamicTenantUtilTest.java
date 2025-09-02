package com.wzkris.user.utils;

import com.wzkris.auth.rmi.domain.LoginUser;
import com.wzkris.common.orm.plus.config.TenantProperties;
import com.wzkris.common.orm.utils.DynamicTenantUtil;
import com.wzkris.user.mapper.TenantInfoMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.OAuth2AccessToken;

import java.time.Instant;
import java.util.Collections;

@DisplayName("租户工具测试用例")
@SpringBootTest
public class DynamicTenantUtilTest {

    TenantProperties tenantProperties;

    public void DynamicTenantUtilTest(TenantProperties tenantProperties) {
        this.tenantProperties = tenantProperties;
        this.tenantProperties.getIncludes().add("t_sys_test");
    }

    static final String SQL = "SELECT * FROM t_sys_user WHERE user_id=?";

    static {
        LoginUser user = new LoginUser(1L, Collections.singleton("*"));
        user.setTenantId(0L);
        OAuth2AccessToken oAuth2AccessToken = new OAuth2AccessToken(
                OAuth2AccessToken.TokenType.BEARER, "xxxxxx", Instant.MIN, Instant.MAX, Collections.emptySet());
        SecurityContextHolder.getContext()
                .setAuthentication(
                        new UsernamePasswordAuthenticationToken(user, ""));
    }

    @Autowired
    TenantInfoMapper tenantMapper;

    @Test
    public void test() {
        DynamicTenantUtil.ignore(() -> {
            listIgnore();
            list(); // 应该不带租户ID
        });
    }

    void listIgnore() {
        DynamicTenantUtil.ignore(this::list);
    }

    void list() {
        tenantMapper.selectList(null);
    }

}
