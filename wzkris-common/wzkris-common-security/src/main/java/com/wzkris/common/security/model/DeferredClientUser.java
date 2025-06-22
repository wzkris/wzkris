package com.wzkris.common.security.model;

import com.wzkris.auth.rmi.domain.ClientUser;

import java.util.function.Supplier;

/**
 * 延迟获取用户信息
 *
 * @author wzkris
 * @date 2025/06/22
 */
public class DeferredClientUser extends ClientUser {

    private final Supplier<ClientUser> supplier;

    private ClientUser clientUser;

    public DeferredClientUser(Long userId,
                              Supplier<ClientUser> supplier) {
        super(userId);
        this.supplier = supplier;
    }

    @Override
    public String getPhoneNumber() {
        init();
        return this.clientUser.getPhoneNumber();
    }

    private void init() {
        if (this.clientUser != null) {
            return;
        }
        this.clientUser = this.supplier.get();
    }

}
