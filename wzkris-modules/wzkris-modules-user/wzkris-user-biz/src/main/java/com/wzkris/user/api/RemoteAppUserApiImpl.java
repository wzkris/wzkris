package com.wzkris.user.api;

import com.wzkris.common.core.domain.Result;
import com.wzkris.common.core.utils.BeanUtil;
import com.wzkris.common.openfeign.annotation.InnerAuth;
import com.wzkris.common.web.model.BaseController;
import com.wzkris.user.api.domain.request.LoginInfoReq;
import com.wzkris.user.api.domain.response.AppUserResp;
import com.wzkris.user.domain.AppUser;
import com.wzkris.user.domain.AppUserThirdinfo;
import com.wzkris.user.mapper.AppUserMapper;
import com.wzkris.user.mapper.AppUserThirdinfoMapper;
import io.swagger.v3.oas.annotations.Hidden;
import lombok.RequiredArgsConstructor;
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

    private final AppUserThirdinfoMapper appUserThirdinfoMapper;

    @Override
    public Result<AppUserResp> getByPhoneNumber(String phoneNumber) {
        AppUser appUser = appUserMapper.selectByPhoneNumber(phoneNumber);
        AppUserResp userResp = BeanUtil.convert(appUser, AppUserResp.class);
        return ok(userResp);
    }

    @Override
    public Result<AppUserResp> getByOpenid(String openid) {
        AppUserThirdinfo userThirdinfo = appUserThirdinfoMapper.selectByOpenid(openid);
        if (userThirdinfo == null) {
            return ok();
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
