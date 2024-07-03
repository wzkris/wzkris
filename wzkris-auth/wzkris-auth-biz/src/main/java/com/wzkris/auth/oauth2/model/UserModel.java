package com.wzkris.auth.oauth2.model;

import lombok.Getter;
import lombok.Setter;
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
    @Setter
    private Object details;// 用户详细信息

    public UserModel(String username,
                     String password,
                     List<String> authorities,
                     Object details) {
        super(username, password, AuthorityUtils.createAuthorityList(authorities));
        this.details = details;
    }

    public UserModel(String username,
                     String password,
                     Collection<? extends GrantedAuthority> authorities,
                     Object details) {
        super(username, password, authorities);
        this.details = details;
    }
}
