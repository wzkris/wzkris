package com.wzkris.common.security.model;

import com.wzkris.common.core.model.AbsBaseLoginUser;
import lombok.Getter;
import lombok.Setter;

/**
 * C 端用户视图。
 */
@Getter
@Setter
public class CustomerLoginUser extends AbsBaseLoginUser {

    private String phoneNumber;

    @Override
    public String getName() {
        return getUid() == null ? "" : String.valueOf(getUid());
    }

}
