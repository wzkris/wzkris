package com.wzkris.user.api;

import com.wzkris.common.core.domain.Result;
import com.wzkris.common.core.utils.MapstructUtil;
import com.wzkris.common.openfeign.annotation.InnerAuth;
import com.wzkris.user.api.domain.dto.AppUserDTO;
import com.wzkris.user.api.domain.dto.LoginInfoDTO;
import com.wzkris.user.domain.AppUser;
import com.wzkris.user.mapper.AppUserMapper;
import io.swagger.v3.oas.annotations.Hidden;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

import static com.wzkris.common.core.domain.Result.success;

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
    public Result<AppUserDTO> getByPhoneNumber(String phoneNumber) {
        AppUser appUser = appUserMapper.selectByPhoneNumber(phoneNumber);
        AppUserDTO appUserDTO = MapstructUtil.convert(appUser, AppUserDTO.class);
        return success(appUserDTO);
    }

    /**
     * 更新用户登录信息
     */
    @Override
    public void updateLoginInfo(LoginInfoDTO loginInfoDTO) {
        AppUser appUser = new AppUser(loginInfoDTO.getUserId());
        appUser.setLoginIp(loginInfoDTO.getLoginIp());
        appUser.setLoginDate(loginInfoDTO.getLoginDate());

        appUserMapper.updateById(appUser);
    }
}
