package com.wzkris.common.core.context;

import com.wzkris.common.core.model.BaseLoginUser;
import com.wzkris.common.core.model.RoleContext;
import org.springframework.lang.Nullable;

/**
 * 用户上下文读取接口（依赖倒置）
 *
 * <p>供基础设施层（orm/log）以无状态方式读取当前操作者身份，避免底层依赖安全层。
 * 实现由 common-security 提供（直接读 SecurityContextHolder），运行时注入。
 *
 * @author wzkris
 */
public interface UserContextProvider {

    /**
     * 当前登录用户，无登录上下文时返回 null
     */
    @Nullable
    BaseLoginUser getBaseLoginUser();

    /**
     * 当前操作者租户ID，非租户用户或无登录上下文时返回 null
     */
    @Nullable
    Long getTenantId();

    /**
     * 当前操作者角色上下文，无登录上下文时返回 null
     */
    @Nullable
    RoleContext getRoleContext();

}
