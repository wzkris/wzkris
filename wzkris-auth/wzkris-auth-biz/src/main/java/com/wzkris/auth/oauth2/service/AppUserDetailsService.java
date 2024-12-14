package com.wzkris.auth.oauth2.service;

import cn.hutool.core.util.ObjUtil;
import com.wzkris.common.core.constant.CommonConstants;
import com.wzkris.common.core.domain.Result;
import com.wzkris.common.security.oauth2.domain.WzUser;
import com.wzkris.common.security.oauth2.domain.model.LoginApper;
import com.wzkris.common.security.oauth2.enums.UserType;
import com.wzkris.common.security.oauth2.utils.OAuth2ExceptionUtil;
import com.wzkris.user.api.RemoteAppUserApi;
import com.wzkris.user.api.domain.response.AppUserResp;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.core.OAuth2ErrorCodes;
import org.springframework.stereotype.Service;

import java.util.Collections;

/**
 * @author : wzkris
 * @version : V1.0.0
 * @description : 查询app用户信息
 * @date : 2024/04/09 10:53
 */
@Service
@RequiredArgsConstructor
public class AppUserDetailsService implements UserDetailsServiceExt {
    private final RemoteAppUserApi remoteAppUserApi;

    public WzUser loadUserByPhoneNumber(String phoneNumber) {
        Result<AppUserResp> result = remoteAppUserApi.getByPhoneNumber(phoneNumber);
        AppUserResp appUserResp = result.checkData();

        return appUserResp == null ? null : this.checkAndBuild(appUserResp);
    }

    /**
     * 构建登录用户
     */
    private WzUser checkAndBuild(AppUserResp appUserResp) {
        // 校验用户状态
        this.checkAccount(appUserResp);

        LoginApper loginApper = new LoginApper();
        loginApper.setUserId(appUserResp.getUserId());
        loginApper.setPhoneNumber(appUserResp.getPhoneNumber());

        return new WzUser(UserType.APP_USER, loginApper.getPhoneNumber(), loginApper, Collections.emptyList());
    }

    /**
     * 校验用户账号
     */
    private void checkAccount(AppUserResp appUserResp) {
        if (ObjUtil.equals(appUserResp.getStatus(), CommonConstants.STATUS_DISABLE)) {
            OAuth2ExceptionUtil.throwErrorI18n(OAuth2ErrorCodes.INVALID_REQUEST, "oauth2.account.disabled");
        }
    }
}
