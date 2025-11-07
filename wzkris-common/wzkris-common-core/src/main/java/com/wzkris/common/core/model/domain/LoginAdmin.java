package com.wzkris.common.core.model.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.wzkris.common.core.enums.AuthType;
import com.wzkris.common.core.model.MyPrincipal;
import lombok.Getter;
import lombok.ToString;

import java.util.List;
import java.util.Set;

/**
 * @author : wzkris
 * @version : V1.0.1
 * @description : 管理员
 * @date : 2024/6/14 15:30
 */
@Getter
@ToString
public class LoginAdmin extends MyPrincipal {

    private final boolean admin;

    private final String username;

    private final List<Long> deptScopes;

    @JsonCreator
    public LoginAdmin(@JsonProperty("id") Long id,
                      @JsonProperty("permissions") Set<String> permissions,
                      @JsonProperty("admin") boolean admin,
                      @JsonProperty("username") String username,
                      @JsonProperty("deptScopes") List<Long> deptScopes) {
        super(id, AuthType.ADMIN, permissions);
        this.admin = admin;
        this.username = username;
        this.deptScopes = deptScopes;
    }

    @Override
    public String getName() {
        return this.username;
    }

}
