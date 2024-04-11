package com.thingslink.auth.oauth2.service;

import com.thingslink.common.core.exception.BusinessExceptionI18n;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public interface UserDetailsServicePlus extends UserDetailsService {


    default UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        throw new BusinessExceptionI18n("oauth2.unsupport.granttype");
    }

}
