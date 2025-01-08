package com.wzkris.auth.service;

import cn.binarywang.wx.miniapp.api.WxMaService;
import cn.binarywang.wx.miniapp.bean.WxMaJscode2SessionResult;
import cn.hutool.core.util.ObjUtil;
import com.wzkris.auth.oauth2.constants.enums.WechatChannel;
import com.wzkris.common.core.constant.CommonConstants;
import com.wzkris.common.core.domain.Result;
import com.wzkris.common.security.oauth2.domain.model.ClientUser;
import com.wzkris.common.security.oauth2.utils.OAuth2ExceptionUtil;
import com.wzkris.user.api.RemoteAppUserApi;
import com.wzkris.user.api.domain.response.AppUserResp;
import jakarta.annotation.Nullable;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.error.WxErrorException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.oauth2.core.OAuth2ErrorCodes;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class ClientUserService {

    private final RemoteAppUserApi remoteAppUserApi;

    @Autowired
    @Lazy
    private WxMaService wxMaService;

    @Nullable
    public ClientUser getUserByPhoneNumber(String phoneNumber) {
        Result<AppUserResp> result = remoteAppUserApi.getByPhoneNumber(phoneNumber);
        AppUserResp appUserResp = result.checkData();

        return appUserResp == null ? null : this.checkAndBuild(appUserResp);
    }

    @Nullable
    public ClientUser getUserByWechat(String channel, String wxCode) {
        if (WechatChannel.XCX.name().equals(channel)) {
            WxMaJscode2SessionResult sessionInfo = this.getOpenid(wxCode);
            Result<AppUserResp> userApiByOpenid = remoteAppUserApi.getByOpenid(sessionInfo.getOpenid());
            if (!userApiByOpenid.isSuccess()) {
                log.info("用户不存在，openid：{}", sessionInfo.getOpenid());
                return null;
            }
            AppUserResp appUserResp = userApiByOpenid.getData();
            return this.checkAndBuild(appUserResp);
        }
        else if (WechatChannel.GZH.name().equals(channel)) {

        }
        return null;
    }

    private WxMaJscode2SessionResult getOpenid(String wxCode) {
        try {
            return wxMaService.getUserService().getSessionInfo(wxCode);
        }
        catch (WxErrorException e) {
            OAuth2ExceptionUtil.throwError(OAuth2ErrorCodes.SERVER_ERROR, e.getMessage());
            return null;// never execute
        }
    }

    /**
     * 构建登录用户
     */
    private ClientUser checkAndBuild(AppUserResp appUserResp) {
        // 校验用户状态
        this.checkAccount(appUserResp);

        ClientUser clientUser = new ClientUser();
        clientUser.setUserId(appUserResp.getUserId());
        clientUser.setPhoneNumber(appUserResp.getPhoneNumber());

        return clientUser;
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
