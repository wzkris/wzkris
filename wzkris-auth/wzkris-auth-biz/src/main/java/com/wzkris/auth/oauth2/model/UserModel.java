package com.wzkris.auth.oauth2.model;

import lombok.Getter;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;

import java.io.Serial;
import java.util.List;

/**
 * @author : wzkris
 * @version : V1.0.1
 * @description : 用户模型
 * @date : 2024/5/16 13:08
 */
@Getter
public class UserModel extends User {

    @Serial
    private static final long serialVersionUID = 2518325159639365166L;

    private final Object principal;// 用户详细信息

    public UserModel(String username,
                     String password,
                     List<String> authorities,
                     Object principal) {
        super(username, password, AuthorityUtils.createAuthorityList(authorities));
        this.principal = principal;
    }
}
