package com.wzkris.user.api;

import cn.binarywang.wx.miniapp.api.WxMaService;
import com.wzkris.common.core.domain.Result;
import com.wzkris.common.core.enums.BizCode;
import com.wzkris.common.core.exception.ThirdServiceException;
import com.wzkris.common.core.utils.BeanUtil;
import com.wzkris.common.openfeign.annotation.InnerAuth;
import com.wzkris.common.web.model.BaseController;
import com.wzkris.user.api.domain.request.LoginInfoReq;
import com.wzkris.user.api.domain.response.AppUserResp;
import com.wzkris.user.domain.AppUser;
import com.wzkris.user.domain.AppUserThirdinfo;
import com.wzkris.user.mapper.AppUserMapper;
import com.wzkris.user.mapper.AppUserThirdinfoMapper;
import com.wzkris.user.service.AppUserService;
import io.swagger.v3.oas.annotations.Hidden;
import lombok.RequiredArgsConstructor;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.mp.api.WxMpService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.transaction.support.TransactionTemplate;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author : wzkris
 * @version : V1.0.0
 * @description : 内部app用户接口
 * @date : 2024/4/15 16:20
 */
@Hidden
@InnerAuth
@RestController
@RequiredArgsConstructor
public class RemoteAppUserApiImpl extends BaseController implements RemoteAppUserApi {

    private final AppUserMapper appUserMapper;

    private final AppUserService appUserService;

    private final AppUserThirdinfoMapper appUserThirdinfoMapper;

    private final TransactionTemplate transactionTemplate;

    @Autowired
    @Lazy
    private WxMaService wxMaService;

    @Autowired
    @Lazy
    private WxMpService wxMpService;

    @Override
    public Result<AppUserResp> getByPhoneNumber(String phoneNumber) {
        AppUser appUser = appUserMapper.selectByPhoneNumber(phoneNumber);
        AppUserResp userResp = BeanUtil.convert(appUser, AppUserResp.class);
        return ok(userResp);
    }

    @Override
    public Result<AppUserResp> getOrRegisterByIdentifier(String identifierType, String authCode) {
        AppUserThirdinfo.IdentifierType type;
        try {
            type = AppUserThirdinfo.IdentifierType.valueOf(identifierType);
        } catch (IllegalArgumentException e) {
            return resp(BizCode.BAD_REQUEST.value(), "identifierType is illegal");
        }

        String identifier;
        switch (type) {
            case WX_XCX -> {
                try {
                    identifier = wxMaService.getUserService().getSessionInfo(authCode).getOpenid();
                } catch (WxErrorException e) {
                    throw new ThirdServiceException(e.getError().getErrorMsg());
                }

            }
            case WX_GZH -> {
                try {
                    identifier = wxMpService.getOAuth2Service().getAccessToken(authCode).getOpenId();
                } catch (WxErrorException e) {
                    throw new ThirdServiceException(e.getError().getErrorMsg());
                }
            }
            default -> identifier = null;
        }

        AppUserThirdinfo userThirdinfo = appUserThirdinfoMapper.selectByIdentifier(identifier);
        if (userThirdinfo == null) {
            userThirdinfo = transactionTemplate.execute(status -> {
                AppUser appUser = new AppUser();
                appUserService.insertUser(appUser);

                AppUserThirdinfo userthirdinfo = new AppUserThirdinfo();
                userthirdinfo.setUserId(appUser.getUserId());
                userthirdinfo.setIdentifier(identifier);
                userthirdinfo.setIdentifierType(AppUserThirdinfo.IdentifierType.WX_XCX.getValue());
                appUserThirdinfoMapper.insert(userthirdinfo);
                return userthirdinfo;
            });
        }
        AppUser appUser = appUserMapper.selectById(userThirdinfo.getUserId());
        AppUserResp userResp = BeanUtil.convert(appUser, AppUserResp.class);
        return ok(userResp);
    }

    @Override
    public void updateLoginInfo(LoginInfoReq loginInfoReq) {
        AppUser appUser = new AppUser(loginInfoReq.getUserId());
        appUser.setLoginIp(loginInfoReq.getLoginIp());
        appUser.setLoginDate(loginInfoReq.getLoginDate());

        appUserMapper.updateById(appUser);
    }
}
