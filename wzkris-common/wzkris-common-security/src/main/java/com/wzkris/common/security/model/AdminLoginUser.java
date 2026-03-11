package com.wzkris.common.security.model;

import com.wzkris.common.core.model.AbsBaseLoginUser;
import lombok.Getter;
import lombok.Setter;
import org.springframework.lang.Nullable;

/**
 * 管理员用户视图。
 */
@Getter
@Setter
public class AdminLoginUser extends AbsBaseLoginUser {

    private String username;

    @Nullable
    private String phoneNumber;

    @Override
    public String getName() {
        return username;
    }

}
