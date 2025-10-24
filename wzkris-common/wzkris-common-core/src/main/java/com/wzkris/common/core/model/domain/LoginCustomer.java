package com.wzkris.common.core.model.domain;

import com.wzkris.common.core.enums.AuthType;
import com.wzkris.common.core.model.MyPrincipal;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Collections;
import java.util.Set;

/**
 * @author : wzkris
 * @version : V1.0.1
 * @description : 客户端用户
 * @date : 2024/6/14 15:30
 */
@Setter
@Getter
@ToString
public class LoginCustomer extends MyPrincipal {

    /**
     * 手机号
     */
    private String phoneNumber;

    public LoginCustomer() {
        this(null);
    }

    public LoginCustomer(Long customerId) {
        this(customerId, Collections.emptySet());
    }

    public LoginCustomer(Long customerId, Set<String> permissions) {
        super(customerId, AuthType.CUSTOMER.getValue(), permissions);
    }

    @Override
    public String getName() {
        return this.phoneNumber;
    }

}
