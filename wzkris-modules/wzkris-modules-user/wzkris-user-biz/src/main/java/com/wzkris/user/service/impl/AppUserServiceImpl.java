package com.wzkris.user.service.impl;

import cn.binarywang.wx.miniapp.api.WxMaService;
import com.wzkris.user.domain.AppUser;
import com.wzkris.user.domain.AppUserWallet;
import com.wzkris.user.mapper.AppUserMapper;
import com.wzkris.user.mapper.AppUserThirdinfoMapper;
import com.wzkris.user.mapper.AppUserWalletMapper;
import com.wzkris.user.service.AppUserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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

}
