package com.wzkris.auth.oauth2.service.impl;

import cn.hutool.core.util.ObjUtil;
import com.wzkris.auth.oauth2.model.UserModel;
import com.wzkris.auth.oauth2.service.UserDetailsServicePlus;
import com.wzkris.common.core.constant.CommonConstants;
import com.wzkris.common.core.domain.Result;
import com.wzkris.common.security.oauth2.domain.model.LoginApper;
import com.wzkris.common.security.oauth2.utils.OAuth2ExceptionUtil;
import com.wzkris.user.api.RemoteAppUserApi;
import com.wzkris.user.api.domain.dto.CustomerDTO;
import lombok.AllArgsConstructor;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.oauth2.core.OAuth2ErrorCodes;
import org.springframework.stereotype.Service;

/**
 * @author : wzkris
 * @version : V1.0.0
 * @description : 查询app用户信息
 * @date : 2024/04/09 10:53
 */
@Service
@AllArgsConstructor
public class AppUserDetailsService implements UserDetailsServicePlus {
    private final RemoteAppUserApi remoteAppUserApi;

    public UserModel loadUserByPhoneNumber(String phoneNumber) throws UsernameNotFoundException {
        Result<CustomerDTO> result = remoteAppUserApi.getByPhoneNumber(phoneNumber);
        CustomerDTO customerDTO = result.checkData();

        return this.checkAndBuild(customerDTO);
    }

    /**
     * 构建登录用户
     */
    private UserModel checkAndBuild(CustomerDTO customerDTO) {
        // 校验用户状态
        this.checkAccount(customerDTO);


        LoginApper loginApper = new LoginApper();
        loginApper.setUserId(customerDTO.getUserId());
        loginApper.setPhoneNumber(customerDTO.getPhoneNumber());

        return new UserModel(customerDTO.getPhoneNumber(),
                "", AuthorityUtils.NO_AUTHORITIES, loginApper);
    }

    /**
     * 校验用户账号
     */
    private void checkAccount(CustomerDTO customerDTO) {
        if (customerDTO == null) {
            OAuth2ExceptionUtil.throwErrorI18n(OAuth2ErrorCodes.INVALID_REQUEST, "oauth2.account.disabled");// 不能明说账号不存在
        }
        if (ObjUtil.equals(customerDTO.getStatus(), CommonConstants.STATUS_DISABLE)) {
            OAuth2ExceptionUtil.throwErrorI18n(OAuth2ErrorCodes.INVALID_REQUEST, "oauth2.account.disabled");
        }
    }
}
