package com.wzkris.auth.service.impl;

import cn.hutool.core.util.ObjUtil;
import com.wzkris.auth.rmi.domain.ClientUser;
import com.wzkris.auth.rmi.enums.AuthenticatedType;
import com.wzkris.auth.service.UserInfoTemplate;
import com.wzkris.common.captcha.service.CaptchaService;
import com.wzkris.common.core.constant.CommonConstants;
import com.wzkris.common.core.enums.BizCode;
import com.wzkris.common.security.oauth2.utils.OAuth2ExceptionUtil;
import com.wzkris.user.rmi.RmiAppUserFeign;
import com.wzkris.user.rmi.domain.resp.AppUserResp;
import jakarta.annotation.Nullable;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.core.OAuth2ErrorCodes;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class ClientUserService extends UserInfoTemplate {

    private final CaptchaService captchaService;

    private final RmiAppUserFeign rmiAppUserFeign;

    @Nullable
    @Override
    public ClientUser loadUserByPhoneNumber(String phoneNumber) {
        AppUserResp userResp = rmiAppUserFeign.getByPhoneNumber(phoneNumber);

        if (userResp == null) {
            captchaService.lockAccount(phoneNumber, 600);
            return null;
        }

        return this.checkAndBuild(userResp);
    }

    @Nullable
    @Override
    public ClientUser loadUserByWechat(String identifierType, String wxCode) {
        AppUserResp userResp = rmiAppUserFeign.getOrRegisterByIdentifier(identifierType, wxCode);

        if (userResp == null) {
            return null;
        }
        return this.checkAndBuild(userResp);
    }

    @Override
    public boolean checkAuthenticatedType(AuthenticatedType authenticatedType) {
        return AuthenticatedType.CLIENT_USER.equals(authenticatedType);
    }

    /**
     * 构建登录用户
     */
    private ClientUser checkAndBuild(AppUserResp userResp) {
        // 校验用户状态
        this.checkAccount(userResp);

        ClientUser clientUser = new ClientUser(userResp.getUserId(), null);
        clientUser.setPhoneNumber(userResp.getPhoneNumber());

        return clientUser;
    }

    /**
     * 校验用户账号
     */
    private void checkAccount(AppUserResp appUserResp) {
        if (ObjUtil.equals(appUserResp.getStatus(), CommonConstants.STATUS_DISABLE)) {
            OAuth2ExceptionUtil.throwErrorI18n(
                    BizCode.BAD_REQUEST.value(), OAuth2ErrorCodes.INVALID_REQUEST, "oauth2.account.disabled");
        }
    }

}
