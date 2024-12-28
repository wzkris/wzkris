package com.wzkris.user.service.impl;

import cn.binarywang.wx.miniapp.api.WxMaService;
import cn.binarywang.wx.miniapp.bean.WxMaJscode2SessionResult;
import cn.binarywang.wx.miniapp.bean.WxMaPhoneNumberInfo;
import cn.hutool.core.util.ObjUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.wzkris.common.core.exception.ThirdServiceException;
import com.wzkris.user.domain.AppUser;
import com.wzkris.user.domain.AppUserThirdinfo;
import com.wzkris.user.domain.AppUserWallet;
import com.wzkris.user.mapper.AppUserMapper;
import com.wzkris.user.mapper.AppUserThirdinfoMapper;
import com.wzkris.user.mapper.AppUserWalletMapper;
import com.wzkris.user.service.AppUserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.bean.oauth2.WxOAuth2AccessToken;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.mp.api.WxMpService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionTemplate;

@Slf4j
@Service
@RequiredArgsConstructor
public class AppUserServiceImpl implements AppUserService {
    private final AppUserMapper appUserMapper;
    private final AppUserThirdinfoMapper appUserThirdinfoMapper;
    private final AppUserWalletMapper appUserWalletMapper;
    private final TransactionTemplate transactionTemplate;
    @Autowired
    @Lazy
    private WxMaService wxMaService;
    @Autowired
    @Lazy
    private WxMpService wxMpService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void insertUser(AppUser user) {
        appUserMapper.insert(user);
        AppUserWallet wallet = new AppUserWallet(user.getUserId());
        appUserWalletMapper.insert(wallet);
    }

    @Override
    public void registerByXcx(String jscode, String code) {
        WxMaJscode2SessionResult sessionInfo;
        try {
            sessionInfo = wxMaService.getUserService().getSessionInfo(jscode);
        }
        catch (WxErrorException e) {
            throw new ThirdServiceException(e.getError().getErrorMsg());
        }

        AppUserThirdinfo thirdinfo = appUserThirdinfoMapper.selectOne(Wrappers.lambdaQuery(AppUserThirdinfo.class)
                .eq(AppUserThirdinfo::getOpenid, sessionInfo.getOpenid()));

        if (thirdinfo != null) {
            throw new ThirdServiceException("用户已注册");
        }

        WxMaPhoneNumberInfo phoneNumber;
        try {
            phoneNumber = wxMaService.getUserService().getPhoneNumber(code);
        }
        catch (WxErrorException e) {
            log.error("获取小程序用户手机号发生异常，errmsg：{}", e.getError());
            throw new ThirdServiceException(e.getError().getErrorMsg());
        }

        transactionTemplate.executeWithoutResult(status -> {
            AppUser appUser = new AppUser();
            appUser.setPhoneNumber(phoneNumber.getPurePhoneNumber());
            this.insertUser(appUser);

            AppUserThirdinfo userThirdinfo = new AppUserThirdinfo();
            userThirdinfo.setUserId(appUser.getUserId());
            userThirdinfo.setAppId(wxMaService.getWxMaConfig().getAppid());
            userThirdinfo.setOpenid(sessionInfo.getOpenid());
            userThirdinfo.setChannel(AppUserThirdinfo.Channel.WX_XCX.getValue());
            appUserThirdinfoMapper.insert(userThirdinfo);
        });
    }

    @Override
    public void registerByGzh(String code) {
        WxOAuth2AccessToken accessToken;
        try {
            accessToken = wxMpService.getOAuth2Service().getAccessToken(code);
        }
        catch (WxErrorException e) {
            throw new ThirdServiceException(e.getError().getErrorMsg());
        }

        AppUserThirdinfo thirdinfo = appUserThirdinfoMapper.selectOne(Wrappers.lambdaQuery(AppUserThirdinfo.class)
                .eq(AppUserThirdinfo::getOpenid, accessToken.getOpenId()));

        if (thirdinfo != null) {
            throw new ThirdServiceException("用户已注册");
        }

        if (ObjUtil.equals(accessToken.getSnapshotUser(), 1)) {//虚拟账号
            // TODO 应当生成临时账号信息
            return;
        }

        transactionTemplate.executeWithoutResult(status -> {
            AppUser appUser = new AppUser();
            this.insertUser(appUser);

            AppUserThirdinfo userThirdinfo = new AppUserThirdinfo();
            userThirdinfo.setUserId(appUser.getUserId());
            userThirdinfo.setAppId(wxMaService.getWxMaConfig().getAppid());
            userThirdinfo.setOpenid(accessToken.getOpenId());
            userThirdinfo.setChannel(AppUserThirdinfo.Channel.WX_GZH.getValue());
            appUserThirdinfoMapper.insert(userThirdinfo);
        });
    }

}
