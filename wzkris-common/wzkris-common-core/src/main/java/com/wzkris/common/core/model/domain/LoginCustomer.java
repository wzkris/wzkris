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
 * @description : 客户端用户
 * @date : 2024/6/14 15:30
 */
@Getter
@ToString
@Setter
public class LoginCustomer extends MyPrincipal {

    private String phoneNumber;

    private String wxopenid;

    @JsonCreator
    public LoginCustomer(@JsonProperty("id") Long id,
                         @JsonProperty("permissions") Set<String> permissions) {
        super(id, AuthTypeEnum.CUSTOMER, permissions);
    }

    @Override
    public String getName() {
        return this.phoneNumber;
    }

}
