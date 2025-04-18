package com.wzkris.user.mapperTest;

import com.wzkris.common.orm.utils.DynamicTenantUtil;
import com.wzkris.common.security.oauth2.domain.model.LoginUser;
import com.wzkris.user.mapper.SysTenantMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.server.resource.authentication.BearerTokenAuthentication;

import java.time.Instant;
import java.util.Collections;

@DisplayName("租户工具测试用例")
@SpringBootTest
public class DynamicTenantUtilTest {

    static {
        LoginUser loginUser = new LoginUser("", Collections.singleton("*"));
        loginUser.setTenantId(0L);
        OAuth2AccessToken oAuth2AccessToken = new OAuth2AccessToken(OAuth2AccessToken.TokenType.BEARER,
                "xxxxxx", Instant.MIN, Instant.MAX, Collections.emptySet());
        SecurityContextHolder.getContext().setAuthentication(
                new BearerTokenAuthentication(loginUser, oAuth2AccessToken, Collections.emptyList()));
    }

    @Autowired
    SysTenantMapper tenantMapper;

    @Test
    public void test() {
        DynamicTenantUtil.ignore(() -> {
            list();
            tenantMapper.selectList(null);// 应该不带租户ID
        });
    }

    void list() {
        DynamicTenantUtil.ignore(() -> {
            tenantMapper.selectList(null);
        });
    }
}
