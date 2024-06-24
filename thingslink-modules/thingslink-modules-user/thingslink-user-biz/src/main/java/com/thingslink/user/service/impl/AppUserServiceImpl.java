package com.thingslink.user.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.thingslink.common.core.utils.StringUtil;
import com.thingslink.user.domain.AppUser;
import com.thingslink.user.mapper.AppUserMapper;
import com.thingslink.user.service.AppUserService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class AppUserServiceImpl implements AppUserService {
    private final AppUserMapper appUserMapper;

    @Override
    public List<AppUser> list(AppUser user) {
        LambdaQueryWrapper<AppUser> lqw = this.buildQueryWrapper(user);
        List<AppUser> appUsers = appUserMapper.selectList(lqw);
        return appUsers;
    }

    private LambdaQueryWrapper<AppUser> buildQueryWrapper(AppUser user) {
        return new LambdaQueryWrapper<AppUser>()
                .eq(StringUtil.isNotBlank(user.getStatus()), AppUser::getStatus, user.getStatus())
                .like(StringUtil.isNotBlank(user.getNickname()), AppUser::getNickname, user.getNickname())
                .like(StringUtil.isNotBlank(user.getPhoneNumber()), AppUser::getPhoneNumber, user.getPhoneNumber())
                ;
    }
}
