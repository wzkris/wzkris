package com.wzkris.common.core.model.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.wzkris.common.core.enums.AuthTypeEnum;
import com.wzkris.common.core.model.MyPrincipal;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Set;

/**
 * @author : wzkris
 * @version : V1.0.1
 * @description : 租户登录信息
 * @date : 2025/10/9 13:30
 */
@Getter
@ToString
@Setter
public class LoginTenant extends MyPrincipal {

    private boolean admin;

    private String username;

    private Long tenantId;

    @JsonCreator
    public LoginTenant(@JsonProperty("id") Long id,
                       @JsonProperty("permissions") Set<String> permissions) {
        super(id, AuthTypeEnum.TENANT, permissions);
    }

    @Override
    public String getName() {
        return this.username;
    }

}
