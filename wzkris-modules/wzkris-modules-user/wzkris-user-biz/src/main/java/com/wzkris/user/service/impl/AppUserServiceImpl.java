package com.wzkris.user.service.impl;

import com.wzkris.user.domain.AppUser;
import com.wzkris.user.domain.AppUserWallet;
import com.wzkris.user.mapper.AppUserMapper;
import com.wzkris.user.mapper.AppUserWalletMapper;
import com.wzkris.user.service.AppUserService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@AllArgsConstructor
public class AppUserServiceImpl implements AppUserService {
    private final AppUserMapper appUserMapper;
    private final AppUserWalletMapper appUserWalletMapper;
//    private final WxMaService wxMaService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void insertUser(AppUser user) {
        appUserMapper.insert(user);
        AppUserWallet wallet = new AppUserWallet(user.getUserId());
        appUserWalletMapper.insert(wallet);
    }

    @Override
    public void registerByXcx(String xcxCode) {
//        WxMaJscode2SessionResult sessionInfo = null;
//        try {
//            sessionInfo = wxMaService.getUserService().getSessionInfo(xcxCode);
//        }
//        catch (WxErrorException e) {
//            log.error("获取小程序用户openid发生异常，errmsg：{}", e.getError());
//            throw new ThirdServiceException(e.getError().getErrorMsg());
//        }
//
//        if (sessionInfo == null) {
//            throw new ThirdServiceException("获取小程序用户openid失败");
//        }


    }

}
