package com.wzkris.common.core.model.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.wzkris.common.core.enums.AuthTypeEnum;
import com.wzkris.common.core.model.MyPrincipal;
import lombok.Getter;
import lombok.Setter;
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
@Setter
public class LoginAdmin extends MyPrincipal {

    private boolean admin;

    private String username;

    private List<Long> deptScopes;

    @JsonCreator
    public LoginAdmin(@JsonProperty("id") Long id,
                      @JsonProperty("permissions") Set<String> permissions) {
        super(id, AuthTypeEnum.ADMIN, permissions);
    }

    @Override
    public String getName() {
        return this.username;
    }

}
