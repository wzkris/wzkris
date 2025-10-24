package com.wzkris.principal.utils;

import com.wzkris.common.core.model.domain.LoginUser;
import com.wzkris.common.orm.plus.config.TenantProperties;
import com.wzkris.common.orm.utils.SkipTenantInterceptorUtil;
import com.wzkris.principal.mapper.TenantInfoMapper;
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
public class TenantContextUtilTest {

    static final String SQL = "SELECT * FROM t_sys_user WHERE user_id=?";

    static {
        LoginUser user = new LoginUser(1L, Collections.singleton("*"), true, "admin", Collections.emptyList());
        OAuth2AccessToken oAuth2AccessToken = new OAuth2AccessToken(
                OAuth2AccessToken.TokenType.BEARER, "xxxxxx", Instant.MIN, Instant.MAX, Collections.emptySet());
        SecurityContextHolder.getContext()
                .setAuthentication(
                        new UsernamePasswordAuthenticationToken(user, ""));
    }

    TenantProperties tenantProperties;

    @Autowired
    TenantInfoMapper tenantMapper;

    public void DynamicTenantUtilTest(TenantProperties tenantProperties) {
        this.tenantProperties = tenantProperties;
        this.tenantProperties.getIncludes().add("t_sys_test");
    }

    @Test
    public void test() {
        SkipTenantInterceptorUtil.ignore(() -> {
            listIgnore();
            list(); // 应该不带租户ID
        });
    }

    void listIgnore() {
        SkipTenantInterceptorUtil.ignore(this::list);
    }

    void list() {
        tenantMapper.selectList(null);
    }

}
