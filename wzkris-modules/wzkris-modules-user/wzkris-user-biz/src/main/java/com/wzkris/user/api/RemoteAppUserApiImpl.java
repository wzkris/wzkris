package com.wzkris.user.api;

import com.wzkris.common.core.domain.Result;
import com.wzkris.common.core.utils.MapstructUtil;
import com.wzkris.common.openfeign.annotation.InnerAuth;
import com.wzkris.user.api.domain.request.LoginInfoReq;
import com.wzkris.user.api.domain.response.AppUserResp;
import com.wzkris.user.domain.AppUser;
import com.wzkris.user.mapper.AppUserMapper;
import io.swagger.v3.oas.annotations.Hidden;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

import static com.wzkris.common.core.domain.Result.ok;

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
public class RemoteAppUserApiImpl implements RemoteAppUserApi {
    private final AppUserMapper appUserMapper;

    /**
     * 根据手机号查询app用户
     */
    @Override
    public Result<AppUserResp> getByPhoneNumber(String phoneNumber) {
        AppUser appUser = appUserMapper.selectByPhoneNumber(phoneNumber);
        AppUserResp appUserResp = MapstructUtil.convert(appUser, AppUserResp.class);
        return ok(appUserResp);
    }

    /**
     * 更新用户登录信息
     */
    @Override
    public void updateLoginInfo(LoginInfoReq loginInfoReq) {
        AppUser appUser = new AppUser(loginInfoReq.getUserId());
        appUser.setLoginIp(loginInfoReq.getLoginIp());
        appUser.setLoginDate(loginInfoReq.getLoginDate());

        appUserMapper.updateById(appUser);
    }
}
