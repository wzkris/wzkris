package com.wzkris.user.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.wzkris.common.core.utils.StringUtil;
import com.wzkris.user.domain.AppUser;
import com.wzkris.user.domain.AppUserWallet;
import com.wzkris.user.mapper.AppUserMapper;
import com.wzkris.user.mapper.AppUserWalletMapper;
import com.wzkris.user.service.AppUserService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@AllArgsConstructor
public class AppUserServiceImpl implements AppUserService {
    private final AppUserMapper appUserMapper;
    private final AppUserWalletMapper appUserWalletMapper;

    @Override
    public List<AppUser> list(AppUser user) {
        LambdaQueryWrapper<AppUser> lqw = this.buildQueryWrapper(user);
        List<AppUser> appUsers = appUserMapper.selectList(lqw);
        return appUsers;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void insertUser(AppUser user) {
        appUserMapper.insert(user);
        AppUserWallet wallet = new AppUserWallet(user.getUserId());
        appUserWalletMapper.insert(wallet);
    }

    private LambdaQueryWrapper<AppUser> buildQueryWrapper(AppUser user) {
        return new LambdaQueryWrapper<AppUser>()
                .eq(StringUtil.isNotBlank(user.getStatus()), AppUser::getStatus, user.getStatus())
                .like(StringUtil.isNotBlank(user.getNickname()), AppUser::getNickname, user.getNickname())
                .like(StringUtil.isNotBlank(user.getPhoneNumber()), AppUser::getPhoneNumber, user.getPhoneNumber())
                ;
    }
}
