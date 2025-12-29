package com.wzkris.common.security.utils;

import com.wzkris.common.core.model.UserPrincipal;
import jakarta.annotation.Nullable;
import org.springframework.util.PatternMatchUtils;

import java.util.Collection;

/**
 * SPEL权限处理
 *
 * @author wzkris
 */
public class PermissionUtil {

    public static boolean hasPerms(String... permissions) {
        UserPrincipal principal = SecurityUtil.getPrincipal();

        if (principal == null) {
            return false;
        }

        return hasPerms(principal.getPerms(), permissions);
    }

    /**
     * 判断接口是否有权限 AND
     *
     * @param list        权限
     * @param permissions 权限码数组
     * @return {boolean}
     */
    public static boolean hasPerms(@Nullable Collection<String> list, @Nullable String... permissions) {
        if (permissions == null) {
            return true;
        }

        if (list == null || list.isEmpty()) {
            return false;
        }

        for (String permission : permissions) {
            if (!hasElement(list, permission)) {
                return false;
            }
        }

        return true;
    }

    public static boolean hasPermsOr(String... permissions) {
        UserPrincipal principal = SecurityUtil.getPrincipal();

        if (principal == null) {
            return false;
        }

        return hasPermsOr(principal.getPerms(), permissions);
    }

    /**
     * 判断：当前账号是否含有权限 OR
     *
     * @param list        权限
     * @param permissions 权限码数组
     * @return true 或 false
     */
    public static boolean hasPermsOr(@Nullable Collection<String> list, @Nullable String... permissions) {
        if (permissions == null) {
            return true;
        }

        if (list == null || list.isEmpty()) {
            return false;
        }

        for (String permission : permissions) {
            if (hasElement(list, permission)) {
                return true;
            }
        }

        return false;
    }

    /**
     * 判断：集合中是否包含指定元素（模糊匹配）
     *
     * @param list    集合
     * @param element 元素
     * @return /
     */
    public static boolean hasElement(Collection<String> list, String element) {
        // 先尝试一下简单匹配，如果可以匹配成功则无需继续模糊匹配
        if (list.contains(element)) {
            return true;
        }

        // 开始模糊匹配
        for (String pattern : list) {
            if (vagueMatch(pattern, element)) {
                return true;
            }
        }

        // 走出for循环说明没有一个元素可以匹配成功
        return false;
    }

    /**
     * 字符串模糊匹配
     * <p>example:
     * <p> user* user-add   --  true
     * <p> user* art-add    --  false
     *
     * @param pattern 表达式
     * @param str     待匹配的字符串
     * @return 是否可以匹配
     */
    public static boolean vagueMatch(String pattern, String str) {
        if (pattern == null && str == null) {
            return true;
        }
        if (pattern == null || str == null) {
            return false;
        }
        return PatternMatchUtils.simpleMatch(pattern, str);
    }

}
