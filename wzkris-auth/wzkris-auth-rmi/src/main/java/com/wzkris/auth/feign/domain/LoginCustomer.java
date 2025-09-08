package com.wzkris.auth.feign.domain;

import com.wzkris.auth.feign.enums.AuthType;
import com.wzkris.common.core.domain.CorePrincipal;
import lombok.Getter;
import lombok.Setter;

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
public class LoginCustomer extends CorePrincipal {

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

}
