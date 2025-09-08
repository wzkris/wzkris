package com.wzkris.common.security.model;

import com.wzkris.auth.feign.domain.LoginCustomer;

import java.util.function.Supplier;

/**
 * 延迟获取用户信息
 *
 * @author wzkris
 * @date 2025/06/22
 */
public class DeferredLoginCustomer extends LoginCustomer {

    private final Supplier<LoginCustomer> supplier;

    private LoginCustomer loginCustomer;

    public DeferredLoginCustomer(Long userId,
                                 Supplier<LoginCustomer> supplier) {
        super(userId);
        this.supplier = supplier;
    }

    @Override
    public String getPhoneNumber() {
        init();
        return this.loginCustomer.getPhoneNumber();
    }

    private void init() {
        if (this.loginCustomer != null) {
            return;
        }
        this.loginCustomer = this.supplier.get();
    }

}
