package com.wzkris.common.security.model;

import com.wzkris.common.core.model.AbsBaseLoginUser;
import lombok.Getter;
import lombok.Setter;

/**
 * OAuth2 客户端主体视图。
 */
@Getter
@Setter
public class ClientLoginUser extends AbsBaseLoginUser {

    private String clientId;

    @Override
    public String getName() {
        return clientId;
    }

}
