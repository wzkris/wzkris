package com.wzkris.common.security.utils;

import com.wzkris.common.core.enums.AuthTypeEnum;
import com.wzkris.common.core.exception.token.TokenExpiredException;
import com.wzkris.common.core.model.BaseLoginUser;
import com.wzkris.common.core.model.RoleContext;
import com.wzkris.common.core.utils.StringUtil;
import com.wzkris.common.security.authentication.RoleContextAuthenticationToken;
import org.springframework.lang.Nullable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContextHolderStrategy;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * @author : wzkris
 * @version : V1.0.0
 * @description : 安全工具
 * @create : 2024/04/22 12:22
 * @update : 2024/12/20 16:35
 */
@Component("su")
public final class SecurityUtil {

    private static final SecurityContextHolderStrategy securityContextHolderStrategy = SecurityContextHolder
            .getContextHolderStrategy();

    @Nullable
    public static Authentication getAuthentication() {
        return securityContextHolderStrategy.getContext().getAuthentication();
    }

    public static void setAuthentication(Authentication authentication) {
        securityContextHolderStrategy.getContext().setAuthentication(authentication);
    }

    /**
     * 获取当前登录用户信息,未登录抛出异常
     *
     * @return 当前用户（实际运行时类型可能为具体 BaseUser 子类）
     */
    public static BaseLoginUser getLoginUser() {
        try {
            Authentication authentication = securityContextHolderStrategy.getContext().getAuthentication();
            return (BaseLoginUser) authentication.getPrincipal();
        } catch (Exception e) {
            throw new TokenExpiredException(401, "forbidden.accessDenied.tokenExpired");
        }
    }

    /**
     * 获取当前登录用户并转换为指定子类，类型不匹配时抛出异常。
     */
    public static <T extends BaseLoginUser> T getLoginUser(Class<T> loginUserClass) {
        Objects.requireNonNull(loginUserClass, "loginUserClass must not be null");
        BaseLoginUser loginUser = getLoginUser();
        if (loginUserClass.isInstance(loginUser)) {
            return loginUserClass.cast(loginUser);
        }
        throw new TokenExpiredException(401, "forbidden.accessDenied.tokenExpired");
    }

    /**
     * 获取当前登录用户的角色上下文
     *
     * @return 角色上下文
     */
    @Nullable
    public static RoleContext getRoleContext() {
        Authentication authentication = securityContextHolderStrategy.getContext().getAuthentication();
        if (authentication instanceof RoleContextAuthenticationToken token) {
            return token.getRoleContext();
        }
        throw new TokenExpiredException(401, "forbidden.accessDenied.tokenExpired");
    }

    /**
     * 获取当前登录用户权限,未登录抛出异常
     *
     * @return 当前用户
     */
    public static Set<String> getPermission() {
        RoleContext roleContext = getRoleContext();
        if (roleContext == null) {
            return Collections.emptySet();
        }
        List<String> authorities = roleContext.getGrantedAuthority();
        return authorities != null ? new LinkedHashSet<>(authorities) : Collections.emptySet();
    }

    /**
     * 判断是否管理员用户
     *
     * @return 管理员用户
     */
    public static boolean isSuperUser() {
        RoleContext roleContext = getRoleContext();
        if (roleContext == null) {
            return false;
        }
        return roleContext.isSuperUser();
    }

    /**
     * 是否认证
     */
    public static boolean isAuth() {
        Authentication authentication = securityContextHolderStrategy.getContext().getAuthentication();
        return authentication != null
                && authentication.isAuthenticated()
                && authentication.getPrincipal() instanceof BaseLoginUser;
    }

    /**
     * 是否对应认证类型
     */
    public static boolean isAuth(AuthTypeEnum authTypeEnum) {
        Authentication authentication = securityContextHolderStrategy.getContext().getAuthentication();
        return authentication != null
                && authentication.isAuthenticated()
                && authentication.getPrincipal() instanceof BaseLoginUser
                && ((BaseLoginUser) authentication.getPrincipal()).getAuthType() == authTypeEnum;
    }

    /**
     * 获取请求的token
     */
    public static String getTokenValue() {
        try {
            Authentication authentication = securityContextHolderStrategy.getContext().getAuthentication();
            return authentication == null ? null : authentication.getCredentials().toString();
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 获取当前ID
     *
     * @return 登录ID
     */
    public static Long getUid() {
        return getLoginUser().getUid();
    }

    /**
     * 获取当前认证类型,未登录抛出异常
     *
     * @return 登录类型
     */
    public static AuthTypeEnum getAuthType() {
        return getLoginUser().getAuthType();
    }

    /**
     * 获取当前标签
     *
     * @return 标签
     */
    public static String getHint() {
        return StringUtil.defaultIfEmpty(getLoginUser().getHint(), StringUtil.EMPTY);
    }

}
