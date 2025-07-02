package com.wzkris.user.rmi;

import cn.binarywang.wx.miniapp.api.WxMaService;
import com.wzkris.common.core.exception.service.ThirdServiceException;
import com.wzkris.common.core.utils.BeanUtil;
import com.wzkris.user.domain.AppUser;
import com.wzkris.user.domain.AppUserThirdinfo;
import com.wzkris.user.mapper.AppUserMapper;
import com.wzkris.user.mapper.AppUserThirdinfoMapper;
import com.wzkris.user.rmi.domain.req.LoginInfoReq;
import com.wzkris.user.rmi.domain.resp.AppUserResp;
import com.wzkris.user.service.AppUserService;
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
@RestController
@RequiredArgsConstructor
public class RmiAppUserFeignImpl implements RmiAppUserFeign {

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
    public AppUserResp getByPhoneNumber(String phoneNumber) {
        AppUser appUser = appUserMapper.selectByPhoneNumber(phoneNumber);
        return BeanUtil.convert(appUser, AppUserResp.class);
    }

    @Override
    public AppUserResp getOrRegisterByIdentifier(String identifierType, String authCode) {
        AppUserThirdinfo.IdentifierType type = AppUserThirdinfo.IdentifierType.valueOf(identifierType);

        String identifier;
        try {
            switch (type) {
                case WX_XCX -> {
                    identifier = wxMaService
                            .getUserService()
                            .getSessionInfo(authCode)
                            .getOpenid();
                }
                case WX_GZH -> {
                    identifier = wxMpService
                            .getOAuth2Service()
                            .getAccessToken(authCode)
                            .getOpenId();
                }
                default -> identifier = null;
            }
        } catch (WxErrorException e) {
            throw new ThirdServiceException(e.getError().getErrorMsg());
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
        return BeanUtil.convert(appUser, AppUserResp.class);
    }

    @Override
    public void updateLoginInfo(LoginInfoReq loginInfoReq) {
        AppUser appUser = new AppUser(loginInfoReq.getUserId());
        appUser.setLoginIp(loginInfoReq.getLoginIp());
        appUser.setLoginDate(loginInfoReq.getLoginDate());

        appUserMapper.updateById(appUser);
    }

}
