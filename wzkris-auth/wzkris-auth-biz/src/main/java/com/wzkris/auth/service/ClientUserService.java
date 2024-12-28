package com.wzkris.auth.service;

import cn.hutool.core.util.ObjUtil;
import com.wzkris.common.core.constant.CommonConstants;
import com.wzkris.common.core.domain.Result;
import com.wzkris.common.security.oauth2.domain.model.ClientUser;
import com.wzkris.common.security.oauth2.utils.OAuth2ExceptionUtil;
import com.wzkris.user.api.RemoteAppUserApi;
import com.wzkris.user.api.domain.response.AppUserResp;
import jakarta.annotation.Nullable;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.core.OAuth2ErrorCodes;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ClientUserService {

    private final RemoteAppUserApi remoteAppUserApi;

    @Nullable
    public ClientUser getUserByPhoneNumber(String phoneNumber) {
        Result<AppUserResp> result = remoteAppUserApi.getByPhoneNumber(phoneNumber);
        AppUserResp appUserResp = result.checkData();

        return appUserResp == null ? null : this.checkAndBuild(appUserResp);
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
