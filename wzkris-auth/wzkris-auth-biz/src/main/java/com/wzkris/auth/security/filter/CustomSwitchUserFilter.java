package com.wzkris.auth.security.filter;

import com.wzkris.auth.constants.OAuth2ParameterConstant;
import com.wzkris.auth.enums.BizLoginCodeEnum;
import com.wzkris.auth.enums.LoginTypeEnum;
import com.wzkris.auth.security.handler.DefaultAuthenticationSuccessHandlerImpl;
import com.wzkris.auth.service.SwitchUserService;
import com.wzkris.auth.service.TokenService;
import com.wzkris.common.core.enums.AuthTypeEnum;
import com.wzkris.common.core.enums.BizBaseCodeEnum;
import com.wzkris.common.core.model.BaseLoginUser;
import com.wzkris.common.core.utils.StringUtil;
import com.wzkris.common.security.handler.AuthenticationEntryPointImpl;
import com.wzkris.common.security.model.LoginAdminUser;
import com.wzkris.common.security.model.LoginTenantUser;
import com.wzkris.common.security.utils.OAuth2ExceptionUtil;
import com.wzkris.common.security.utils.SecurityUtil;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.oauth2.core.OAuth2ErrorCodes;
import org.springframework.security.web.authentication.AuthenticationEntryPointFailureHandler;
import org.springframework.security.web.authentication.switchuser.SwitchUserFilter;
import org.springframework.security.web.context.NullSecurityContextRepository;
import org.springframework.stereotype.Component;

import java.util.Objects;

/**
 * 登录态切换：ADMIN → 租户最高管理员；TENANT（含 actor）→ ADMIN。
 */
@Component
public final class CustomSwitchUserFilter extends SwitchUserFilter {

    private final TokenService tokenService;

    private final SwitchUserService switchUserService;

    public CustomSwitchUserFilter(TokenService tokenService, SwitchUserService switchUserService) {
        super();
        this.tokenService = tokenService;
        this.switchUserService = switchUserService;
        setSuccessHandler(new DefaultAuthenticationSuccessHandlerImpl());
        setFailureHandler(new AuthenticationEntryPointFailureHandler(new AuthenticationEntryPointImpl()));
        setSecurityContextRepository(new NullSecurityContextRepository());
        setUserDetailsService(username -> {
            throw new UsernameNotFoundException("unused: " + username);
        });
    }

    @Override
    protected Authentication attemptSwitchUser(HttpServletRequest request) {
        String loginType = request.getParameter(OAuth2ParameterConstant.LOGIN_TYPE);
        if (!Objects.equals(LoginTypeEnum.SWITCH, LoginTypeEnum.fromValue(loginType))) {
            OAuth2ExceptionUtil.throwErrorI18n(
                    BizBaseCodeEnum.REQUEST_ERROR.value(), OAuth2ErrorCodes.INVALID_REQUEST,
                    "invalidParameter.param.invalid", OAuth2ParameterConstant.LOGIN_TYPE);
        }

        LoginAdminUser adminUser = SecurityUtil.getLoginUser(LoginAdminUser.class);
        return completeSwitch(switchUserService.switchToTenant(adminUser, parseTenantId(request)));
    }

    @Override
    protected Authentication attemptExitUser(HttpServletRequest request) {
        LoginTenantUser tenantUser = SecurityUtil.getLoginUser(LoginTenantUser.class);
        if (tenantUser.getActorUid() == null || tenantUser.getActorAuthType() != AuthTypeEnum.ADMIN) {
            OAuth2ExceptionUtil.throwError(
                    BizLoginCodeEnum.PARAMETER_ERROR.getCode(), OAuth2ErrorCodes.INVALID_REQUEST,
                    "current session is not impersonated by admin");
        }
        return completeSwitch(switchUserService.switchToAdmin(tenantUser.getActorUid()));
    }

    private UsernamePasswordAuthenticationToken completeSwitch(UsernamePasswordAuthenticationToken authenticated) {
        if (authenticated == null) {
            OAuth2ExceptionUtil.throwError(
                    BizLoginCodeEnum.USER_NOT_EXIST.getCode(), OAuth2ErrorCodes.INVALID_REQUEST,
                    "switch target user not found");
        }
        authenticated.setDetails(tokenService.login(
                (BaseLoginUser) authenticated.getPrincipal(),
                AuthorityUtils.authorityListToSet(authenticated.getAuthorities())));
        return authenticated;
    }

    private Long parseTenantId(HttpServletRequest request) {
        String raw = request.getParameter(OAuth2ParameterConstant.TENANT_ID);
        if (StringUtil.isBlank(raw)) {
            OAuth2ExceptionUtil.throwErrorI18n(
                    BizBaseCodeEnum.REQUEST_ERROR.value(), OAuth2ErrorCodes.INVALID_REQUEST,
                    "invalidParameter.param.invalid", OAuth2ParameterConstant.TENANT_ID);
        }
        try {
            return Long.valueOf(raw);
        } catch (NumberFormatException e) {
            OAuth2ExceptionUtil.throwErrorI18n(
                    BizBaseCodeEnum.REQUEST_ERROR.value(), OAuth2ErrorCodes.INVALID_REQUEST,
                    "invalidParameter.param.invalid", OAuth2ParameterConstant.TENANT_ID);
            return null;
        }
    }

}
