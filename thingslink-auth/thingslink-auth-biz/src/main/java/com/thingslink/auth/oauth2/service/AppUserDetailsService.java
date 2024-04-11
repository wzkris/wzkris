package com.thingslink.auth.oauth2.service;

import cn.hutool.core.util.ObjUtil;
import com.thingslink.auth.domain.Customer;
import com.thingslink.auth.mapper.CustomerMapper;
import com.thingslink.common.core.constant.CommonConstants;
import com.thingslink.common.core.exception.BusinessExceptionI18n;
import com.thingslink.common.core.utils.MapstructUtil;
import com.thingslink.common.security.model.AppUser;
import com.thingslink.common.security.utils.OAuth2EndpointUtil;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
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
public class AppUserDetailsService implements UserDetailsService {
    private final CustomerMapper customerMapper;

    public UserDetails loadUserByPhoneNumber(String phoneNumber) throws UsernameNotFoundException {
        Customer customer = customerMapper.selectByPhoneNumber(phoneNumber);

        return this.checkAndBuildAppUser(customer);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        throw new BusinessExceptionI18n("oauth2.unsupport.granttype");
    }

    /**
     * 构建登录用户
     */
    private UserDetails checkAndBuildAppUser(Customer customer) {
        // 校验用户状态
        this.checkAccount(customer);
        // 获取权限信息

        AppUser appUser = MapstructUtil.convert(customer, AppUser.class);

        return appUser;
    }

    /**
     * 校验用户账号
     */
    private void checkAccount(Customer customer) {
        if (customer == null) {
            OAuth2EndpointUtil.throwErrorI18n(OAuth2ErrorCodes.INVALID_REQUEST, "oauth2.passlogin.fail");// 不能明说账号不存在
        }
        if (ObjUtil.equals(customer.getStatus(), CommonConstants.STATUS_DISABLE)) {
            OAuth2EndpointUtil.throwErrorI18n(OAuth2ErrorCodes.INVALID_REQUEST, "oauth2.account.disabled");
        }
    }
}
