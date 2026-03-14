package com.wzkris.auth.security.core.password;

import com.wzkris.auth.constants.OAuth2ParameterConstant;
import com.wzkris.auth.enums.BizLoginCodeEnum;
import com.wzkris.auth.security.core.CommonAuthenticationProvider;
import com.wzkris.auth.security.core.CommonAuthenticationToken;
import com.wzkris.auth.service.AuthRiskFacade;
import com.wzkris.auth.service.LoginUserService;
import com.wzkris.auth.service.TokenService;
import com.wzkris.common.core.enums.BizBaseCodeEnum;
import com.wzkris.common.security.utils.OAuth2ExceptionUtil;
import com.wzkris.risk.httpclient.riskctl.resp.RiskDecisionResp;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.OAuth2ErrorCodes;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

/**
 * @author wzkris
 * @date 2024/3/11
 * @description 密码模式核心处理
 */
@Component // 注册成bean方便引用
public final class PasswordAuthenticationProvider extends CommonAuthenticationProvider {

    private static final int BIZ_RISK_CAPTCHA_REQUIRED = 409_001;

    private static final int BIZ_RISK_BLOCKED = 409_004;

    private final List<LoginUserService> loginUserServices;

    private final AuthRiskFacade authRiskFacade;

    public PasswordAuthenticationProvider(
            TokenService tokenService,
            List<LoginUserService> loginUserServices,
            AuthRiskFacade authRiskFacade) {
        super(tokenService);
        this.loginUserServices = loginUserServices;
        this.authRiskFacade = authRiskFacade;
    }

    @Override
    public CommonAuthenticationToken doAuthenticate(Authentication authentication) {
        PasswordAuthenticationToken authenticationToken = (PasswordAuthenticationToken) authentication;

        Optional<LoginUserService> templateOptional = loginUserServices.stream()
                .filter(t -> t.checkAuthType(authenticationToken.getAuthType()))
                .findFirst();

        if (templateOptional.isEmpty()) {
            OAuth2ExceptionUtil.throwErrorI18n(
                    BizLoginCodeEnum.PARAMETER_ERROR.value(),
                    OAuth2ErrorCodes.INVALID_REQUEST,
                    "invalidParameter.param.invalid",
                    OAuth2ParameterConstant.AUTH_TYPE);
        }

        RiskDecisionResp decision = authRiskFacade.decidePassword(authenticationToken);
        if (decision == null) {
            OAuth2ExceptionUtil.throwError(BizBaseCodeEnum.SYSTEM_ERROR.value(), "service.internalError.error");
        }
        if ("BLOCK".equals(decision.getDecision())) {
            OAuth2ExceptionUtil.throwError(BIZ_RISK_BLOCKED, decision.getMessage());
        }
        if ("CAPTCHA".equals(decision.getDecision())) {
            OAuth2ExceptionUtil.throwError(BIZ_RISK_CAPTCHA_REQUIRED, decision.getMessage());
        }

        CommonAuthenticationToken token = templateOptional.get().loadByUsernameAndPassword(
                authenticationToken.getUsername(), authenticationToken.getPassword());

        if (token == null) {
            // 抛出异常
            OAuth2ExceptionUtil.throwErrorI18n(
                    BizLoginCodeEnum.USER_NOT_EXIST.value(), OAuth2ErrorCodes.INVALID_REQUEST, "oauth2.passlogin.fail");
        }

        return token;
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return PasswordAuthenticationToken.class.isAssignableFrom(authentication);
    }
}
