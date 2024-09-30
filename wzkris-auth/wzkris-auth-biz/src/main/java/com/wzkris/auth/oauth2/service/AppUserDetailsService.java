package com.wzkris.auth.oauth2.service;

import cn.hutool.core.util.ObjUtil;
import com.wzkris.auth.oauth2.model.UserModel;
import com.wzkris.common.core.constant.CommonConstants;
import com.wzkris.common.core.domain.Result;
import com.wzkris.common.core.exception.BusinessExceptionI18n;
import com.wzkris.common.security.oauth2.domain.model.LoginApper;
import com.wzkris.common.security.oauth2.utils.OAuth2ExceptionUtil;
import com.wzkris.user.api.RemoteAppUserApi;
import com.wzkris.user.api.domain.dto.AppUserDTO;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.oauth2.core.OAuth2ErrorCodes;
import org.springframework.stereotype.Service;

import java.util.Collections;

/**
 * @author : wzkris
 * @version : V1.0.0
 * @description : 查询app用户信息
 * @date : 2024/04/09 10:53
 */
@Service
@AllArgsConstructor
public class AppUserDetailsService implements UserDetailsService {
    private final RemoteAppUserApi remoteAppUserApi;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        throw new BusinessExceptionI18n("oauth2.unsupport.granttype");
    }

    public UserModel loadUserByPhoneNumber(String phoneNumber) {
        Result<AppUserDTO> result = remoteAppUserApi.getByPhoneNumber(phoneNumber);
        AppUserDTO appUserDTO = result.checkData();

        return this.checkAndBuild(appUserDTO);
    }

    /**
     * 构建登录用户
     */
    private UserModel checkAndBuild(AppUserDTO appUserDTO) {
        // 校验用户状态
        this.checkAccount(appUserDTO);

        LoginApper loginApper = new LoginApper();
        loginApper.setUserId(appUserDTO.getUserId());
        loginApper.setPhoneNumber(appUserDTO.getPhoneNumber());

        return new UserModel(appUserDTO.getPhoneNumber(),
                "", Collections.emptyList(), loginApper);
    }

    /**
     * 校验用户账号
     */
    private void checkAccount(AppUserDTO appUserDTO) {
        if (appUserDTO == null) {
            OAuth2ExceptionUtil.throwErrorI18n(OAuth2ErrorCodes.INVALID_REQUEST, "oauth2.smslogin.fail");// 不能明说账号不存在
        }
        if (ObjUtil.equals(appUserDTO.getStatus(), CommonConstants.STATUS_DISABLE)) {
            OAuth2ExceptionUtil.throwErrorI18n(OAuth2ErrorCodes.INVALID_REQUEST, "oauth2.account.disabled");
        }
    }
}
