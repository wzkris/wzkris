package com.wzkris.auth.oauth2.model;

import com.wzkris.common.security.oauth2.domain.Loginer;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;
import java.util.List;

/**
 * @author : wzkris
 * @version : V1.0.1
 * @description : 用户模型
 * @date : 2024/5/16 13:08
 */
public class UserModel extends User {

    @Getter
    private final Loginer principal;// 用户详细信息

    public UserModel(String username,
                     String password,
                     List<String> authorities,
                     Loginer principal) {
        super(username, password, AuthorityUtils.createAuthorityList(authorities));
        this.principal = principal;
    }

    public UserModel(String username,
                     String password,
                     Collection<? extends GrantedAuthority> authorities,
                     Loginer principal) {
        super(username, password, authorities);
        this.principal = principal;
    }
}
