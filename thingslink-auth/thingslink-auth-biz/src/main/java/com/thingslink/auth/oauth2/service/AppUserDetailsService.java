package com.thingslink.auth.oauth2.service;

import cn.hutool.core.util.ObjUtil;
import com.thingslink.common.core.constant.CommonConstants;
import com.thingslink.common.core.domain.Result;
import com.thingslink.common.core.utils.MapstructUtil;
import com.thingslink.common.security.model.AppUser;
import com.thingslink.common.security.utils.OAuth2EndpointUtil;
import com.thingslink.user.api.RemoteCustomerApi;
import com.thingslink.user.api.domain.dto.CustomerDTO;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
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
    private final RemoteCustomerApi remoteCustomerApi;

    public UserDetails loadUserByPhoneNumber(String phoneNumber) throws UsernameNotFoundException {
        Result<CustomerDTO> result = remoteCustomerApi.getByPhoneNumber(phoneNumber);
        CustomerDTO customerDTO = result.checkData();

        return this.checkAndBuildAppUser(customerDTO);
    }

    /**
     * 构建登录用户
     */
    private UserDetails checkAndBuildAppUser(CustomerDTO customerDTO) {
        // 校验用户状态
        this.checkAccount(customerDTO);
        // 获取权限信息

        AppUser appUser = MapstructUtil.convert(customerDTO, AppUser.class);

        return appUser;
    }

    /**
     * 校验用户账号
     */
    private void checkAccount(CustomerDTO customerDTO) {
        if (customerDTO == null) {
            OAuth2EndpointUtil.throwErrorI18n(OAuth2ErrorCodes.INVALID_REQUEST, "oauth2.account.disabled");// 不能明说账号不存在
        }
        if (ObjUtil.equals(customerDTO.getStatus(), CommonConstants.STATUS_DISABLE)) {
            OAuth2EndpointUtil.throwErrorI18n(OAuth2ErrorCodes.INVALID_REQUEST, "oauth2.account.disabled");
        }
    }
}
