package com.wzkris.auth.security.filter;

import com.wzkris.auth.constants.OAuth2ParameterConstant;
import com.wzkris.auth.domain.TokenClaims;
import com.wzkris.auth.enums.BizLoginCodeEnum;
import com.wzkris.auth.security.handler.DefaultAuthenticationSuccessHandlerImpl;
import com.wzkris.auth.service.SwitchUserService;
import com.wzkris.auth.service.TokenService;
import com.wzkris.auth.utils.JwtTokenHelper;
import com.wzkris.common.core.enums.AuthTypeEnum;
import com.wzkris.common.core.enums.BizBaseCodeEnum;
import com.wzkris.common.core.model.ActorInfo;
import com.wzkris.common.core.model.BaseLoginUser;
import com.wzkris.common.core.model.LoginUser;
import com.wzkris.common.core.model.RoleContext;
import com.wzkris.common.core.utils.StringUtil;
import com.wzkris.common.security.authentication.RoleContextAuthenticationToken;
import com.wzkris.common.security.handler.AuthenticationEntryPointImpl;
import com.wzkris.common.security.utils.OAuth2ExceptionUtil;
import com.wzkris.common.security.utils.SecurityUtil;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.oauth2.core.OAuth2ErrorCodes;
import org.springframework.security.web.authentication.AuthenticationEntryPointFailureHandler;
import org.springframework.security.web.authentication.switchuser.SwitchUserFilter;
import org.springframework.security.web.context.NullSecurityContextRepository;
import org.springframework.stereotype.Component;

/**
 * 登录态切换：ADMIN -> 租户最高管理员；TENANT（含 actor）-> ADMIN。
 */
@Component
public final class CustomSwitchUserFilter extends SwitchUserFilter {

    private final TokenService tokenService;

    private final SwitchUserService switchUserService;

    private final JwtTokenHelper jwtTokenHelper;

    public CustomSwitchUserFilter(
            TokenService tokenService,
            SwitchUserService switchUserService,
            JwtTokenHelper jwtTokenHelper) {
        super();
        this.tokenService = tokenService;
        this.switchUserService = switchUserService;
        this.jwtTokenHelper = jwtTokenHelper;
        setSuccessHandler(new DefaultAuthenticationSuccessHandlerImpl());
        setFailureHandler(new AuthenticationEntryPointFailureHandler(new AuthenticationEntryPointImpl()));
        setSecurityContextRepository(new NullSecurityContextRepository());
        setUserDetailsService(username -> {
            throw new UsernameNotFoundException("unused: " + username);
        });
    }

    @Override
    protected Authentication attemptSwitchUser(HttpServletRequest request) {
        BaseLoginUser loginUser = SecurityUtil.getLoginUser();
        if (!SecurityUtil.isAuth(AuthTypeEnum.ADMIN)) {
            OAuth2ExceptionUtil.throwErrorI18n(
                    BizBaseCodeEnum.ACCESS_DENIED.value(), OAuth2ErrorCodes.ACCESS_DENIED,
                    "invalidParameter.param.invalid");
            return null;
        }

        TokenClaims claims = jwtTokenHelper.parse(SecurityUtil.getTokenValue());
        return completeEnter(switchUserService.switchToTenant((LoginUser) loginUser, parseTenantId(request), claims.getSid()));
    }

    @Override
    protected Authentication attemptExitUser(HttpServletRequest request) {
        BaseLoginUser loginUser = SecurityUtil.getLoginUser();
        if (!SecurityUtil.isAuth(AuthTypeEnum.TENANT)) {
            OAuth2ExceptionUtil.throwErrorI18n(
                    BizBaseCodeEnum.ACCESS_DENIED.value(), OAuth2ErrorCodes.ACCESS_DENIED,
                    "invalidParameter.param.invalid");
            return null;
        }
        ActorInfo actor = loginUser.getActor();
        if (actor == null || actor.getAuthType() != AuthTypeEnum.ADMIN || StringUtil.isBlank(actor.getSid())) {
            OAuth2ExceptionUtil.throwError(
                    BizLoginCodeEnum.PARAMETER_ERROR.getCode(), OAuth2ErrorCodes.INVALID_REQUEST,
                    "current session is not impersonated by admin");
        }
        UsernamePasswordAuthenticationToken authenticated = switchUserService.switchBack(actor.getUid(), actor.getAuthType());
        if (authenticated == null) {
            OAuth2ExceptionUtil.throwError(
                    BizLoginCodeEnum.USER_NOT_EXIST.getCode(), OAuth2ErrorCodes.INVALID_REQUEST,
                    "switch target user not found");
        }
        BaseLoginUser tenantUser = (BaseLoginUser) authenticated.getPrincipal();
        RoleContext roleContext = authenticated instanceof RoleContextAuthenticationToken rcToken
                ? rcToken.getRoleContext() : null;
        authenticated.setDetails(tokenService.loginReuse(tenantUser, roleContext, actor.getSid()));

        TokenClaims claims = jwtTokenHelper.parse(SecurityUtil.getTokenValue());
        tokenService.revoke(tenantUser, claims.getSid());
        return authenticated;
    }

    private UsernamePasswordAuthenticationToken completeEnter(UsernamePasswordAuthenticationToken authenticated) {
        if (authenticated == null) {
            OAuth2ExceptionUtil.throwError(
                    BizLoginCodeEnum.USER_NOT_EXIST.getCode(), OAuth2ErrorCodes.INVALID_REQUEST,
                    "switch target user not found");
        }
        RoleContext roleContext = authenticated instanceof RoleContextAuthenticationToken rcToken
                ? rcToken.getRoleContext() : null;
        authenticated.setDetails(tokenService.loginCreate(
                (BaseLoginUser) authenticated.getPrincipal(), roleContext));
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
