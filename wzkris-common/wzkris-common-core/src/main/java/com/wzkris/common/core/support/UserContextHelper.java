package com.wzkris.common.core.support;

import com.wzkris.common.core.model.BaseLoginUser;
import com.wzkris.common.core.model.RoleContext;
import org.springframework.lang.Nullable;

/**
 * 用户上下文读取接口
 *
 * @author wzkris
 */
public interface UserContextHelper {

    String BEAN_NAME = "uch";

    /**
     * 当前登录用户，无登录上下文时返回 null
     */
    @Nullable
    BaseLoginUser getLoginUser();

    /**
     * 当前操作者角色上下文，无登录上下文时返回 null
     */
    @Nullable
    RoleContext getRoleContext();

}
