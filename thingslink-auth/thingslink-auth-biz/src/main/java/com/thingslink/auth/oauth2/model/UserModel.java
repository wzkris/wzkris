package com.thingslink.auth.oauth2.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * @author : wzkris
 * @version : V1.0.1
 * @description : 用户模型
 * @date : 2024/5/16 13:08
 */
public class UserModel extends User {

    @Getter
    @Setter
    private Map<String, Object> attributes;// 详细属性

    public UserModel(String username,
                     String password,
                     List<String> authorities,
                     Map<String, Object> attributes) {
        super(username, password, AuthorityUtils.createAuthorityList(authorities));
        this.attributes = attributes;
    }

    public UserModel(String username,
                     String password,
                     Collection<? extends GrantedAuthority> authorities,
                     Map<String, Object> attributes) {
        super(username, password, authorities);
        this.attributes = attributes;
    }
}
