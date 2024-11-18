package com.wzkris.user.service.impl;

import com.wzkris.user.domain.AppUser;
import com.wzkris.user.domain.AppUserWallet;
import com.wzkris.user.mapper.AppUserMapper;
import com.wzkris.user.mapper.AppUserWalletMapper;
import com.wzkris.user.service.AppUserService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
public class AppUserServiceImpl implements AppUserService {
    private final AppUserMapper appUserMapper;
    private final AppUserWalletMapper appUserWalletMapper;


    @Override
    @Transactional(rollbackFor = Exception.class)
    public void insertUser(AppUser user) {
        appUserMapper.insert(user);
        AppUserWallet wallet = new AppUserWallet(user.getUserId());
        appUserWalletMapper.insert(wallet);
    }

}
