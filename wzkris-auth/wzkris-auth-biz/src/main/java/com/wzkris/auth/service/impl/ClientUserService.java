package com.wzkris.auth.service.impl;

import cn.binarywang.wx.miniapp.api.WxMaService;
import cn.binarywang.wx.miniapp.bean.WxMaJscode2SessionResult;
import cn.hutool.core.util.ObjUtil;
import com.wzkris.auth.oauth2.constants.enums.WechatChannel;
import com.wzkris.auth.service.CaptchaService;
import com.wzkris.auth.service.UserInfoTemplate;
import com.wzkris.common.core.constant.CommonConstants;
import com.wzkris.common.core.domain.Result;
import com.wzkris.common.core.enums.BizCode;
import com.wzkris.common.security.oauth2.domain.model.ClientUser;
import com.wzkris.common.security.oauth2.enums.LoginType;
import com.wzkris.common.security.oauth2.utils.OAuth2ExceptionUtil;
import com.wzkris.user.api.RemoteAppUserApi;
import com.wzkris.user.api.domain.response.AppUserResp;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.bean.oauth2.WxOAuth2AccessToken;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.mp.api.WxMpService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.oauth2.core.OAuth2ErrorCodes;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class ClientUserService extends UserInfoTemplate {

    private final CaptchaService captchaService;

    private final RemoteAppUserApi remoteAppUserApi;

    @Autowired
    @Lazy
    private WxMaService wxMaService;

    @Autowired
    @Lazy
    private WxMpService wxMpService;

    @Nullable
    @Override
    public ClientUser loadUserByPhoneNumber(String phoneNumber) {
        Result<AppUserResp> result = remoteAppUserApi.getByPhoneNumber(phoneNumber);
        if (!result.isSuccess()) {
            OAuth2ExceptionUtil.throwError(result.getCode(), result.getMessage());
        }

        AppUserResp userResp = result.getData();

        if (userResp == null) {
            captchaService.lockAccount(phoneNumber);
            return null;
        }

        return this.checkAndBuild(userResp);
    }

    @Nullable
    @Override
    public ClientUser loadUserByWechat(String channel, String wxCode) {
        AppUserResp userResp = null;
        if (WechatChannel.XCX.name().equals(channel)) {
            WxMaJscode2SessionResult sessionInfo;

            try {
                sessionInfo = wxMaService.getUserService().getSessionInfo(wxCode);
            } catch (WxErrorException e) {
                OAuth2ExceptionUtil.throwError(BizCode.THIRD_SERVICE.value(), OAuth2ErrorCodes.SERVER_ERROR, e.getMessage());
                return null;// never execute
            }

            Result<AppUserResp> result = remoteAppUserApi.getByOpenid(sessionInfo.getOpenid());
            if (!result.isSuccess()) {
                OAuth2ExceptionUtil.throwError(result.getCode(), result.getMessage());
            }

            userResp = result.getData();
        } else if (WechatChannel.GZH.name().equals(channel)) {
            WxOAuth2AccessToken accessToken;

            try {
                accessToken = wxMpService.getOAuth2Service().getAccessToken(wxCode);
            } catch (WxErrorException e) {
                OAuth2ExceptionUtil.throwError(BizCode.THIRD_SERVICE.value(), OAuth2ErrorCodes.SERVER_ERROR, e.getMessage());
                return null;// never execute
            }

            Result<AppUserResp> result = remoteAppUserApi.getByOpenid(accessToken.getOpenId());
            if (!result.isSuccess()) {
                OAuth2ExceptionUtil.throwError(result.getCode(), result.getMessage());
            }

            userResp = result.getData();
        }

        if (userResp == null) {
            return null;
        }
        return this.checkAndBuild(userResp);
    }

    @Override
    public boolean checkLoginType(LoginType loginType) {
        return LoginType.CLIENT_USER.equals(loginType);
    }

    /**
     * 构建登录用户
     */
    @Nonnull
    private ClientUser checkAndBuild(@Nonnull AppUserResp userResp) {
        // 校验用户状态
        this.checkAccount(userResp);

        ClientUser clientUser = new ClientUser();
        clientUser.setUserId(userResp.getUserId());
        clientUser.setPhoneNumber(userResp.getPhoneNumber());

        return clientUser;
    }

    /**
     * 校验用户账号
     */
    private void checkAccount(AppUserResp appUserResp) {
        if (ObjUtil.equals(appUserResp.getStatus(), CommonConstants.STATUS_DISABLE)) {
            OAuth2ExceptionUtil.throwErrorI18n(BizCode.BAD_REQUEST.value(), OAuth2ErrorCodes.INVALID_REQUEST, "oauth2.account.disabled");
        }
    }

}
