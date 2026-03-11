package com.wzkris.usercenter.mapper.utils;

import com.wzkris.common.core.enums.AuthTypeEnum;
import com.wzkris.common.core.model.AbsBaseLoginUser;
import com.wzkris.common.orm.plus.config.TenantProperties;
import com.wzkris.common.orm.utils.SkipTenantInterceptorUtil;
import com.wzkris.usercenter.mapper.TenantInfoMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;

@DisplayName("租户工具测试用例")
@SpringBootTest
public class TenantContextUtilTest {

    static final String SQL = "SELECT * FROM t_sys_user WHERE user_id=?";

    static {
        TestLoginUser loginUser = new TestLoginUser();
        loginUser.setUid(1L);
        loginUser.setAuthType(AuthTypeEnum.ADMIN);
        loginUser.setUsername("admin");
        SecurityContextHolder.getContext()
                .setAuthentication(
                        new UsernamePasswordAuthenticationToken(loginUser, ""));
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

    private static final class TestLoginUser extends AbsBaseLoginUser {

        private String username;

        @Override
        public String getUsername() {
            return username;
        }

        @Override
        public String getName() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }
    }

}
