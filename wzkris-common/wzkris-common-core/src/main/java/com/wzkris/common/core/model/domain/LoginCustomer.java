package com.wzkris.common.core.model.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.wzkris.common.core.enums.AuthType;
import com.wzkris.common.core.model.MyPrincipal;
import lombok.Getter;
import lombok.ToString;

import java.util.Set;

/**
 * @author : wzkris
 * @version : V1.0.1
 * @description : 客户端用户
 * @date : 2024/6/14 15:30
 */
@Getter
@ToString
public class LoginCustomer extends MyPrincipal {

    /**
     * 手机号
     */
    private final String phoneNumber;

    @JsonCreator
    public LoginCustomer(@JsonProperty("id") Long id,
                         @JsonProperty("permissions") Set<String> permissions,
                         @JsonProperty("phoneNumber") String phoneNumber) {
        super(id, AuthType.CUSTOMER, permissions);
        this.phoneNumber = phoneNumber;
    }

    @Override
    public String getName() {
        return this.phoneNumber;
    }

}
