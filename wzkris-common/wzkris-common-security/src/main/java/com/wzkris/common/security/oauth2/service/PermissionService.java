package com.wzkris.common.security.oauth2.service;

import org.springframework.lang.NonNull;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Collection;

/**
 * SPEL权限处理
 *
 * @author wzkris
 */
public class PermissionService {

    /**
     * 判断接口是否有权限 AND
     *
     * @param permissions 权限码数组
     * @return {boolean}
     */
    public boolean hasPerms(String... permissions) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null) {
            return false;
        }

        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        if (authorities == null || authorities.isEmpty()) {
            return false;
        }

        if (permissions == null) {
            return true;
        }

        for (String permission : permissions) {
            if (!this.hasElement(authorities, permission)) {
                return false;
            }
        }

        return true;
    }

    /**
     * 判断：当前账号是否含有权限 OR
     *
     * @param permissions 权限码数组
     * @return true 或 false
     */
    public boolean hasPermsOr(String... permissions) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null) {
            return false;
        }

        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        if (authorities == null || authorities.isEmpty()) {
            return false;
        }

        if (permissions == null) {
            return true;
        }

        for (String permission : permissions) {
            if (this.hasElement(authorities, permission)) {
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
    public boolean hasElement(@NonNull Collection<? extends GrantedAuthority> list, @NonNull String element) {
        // 先尝试一下简单匹配，如果可以匹配成功则无需继续模糊匹配
        if (list.contains(new SimpleGrantedAuthority(element))) {
            return true;
        }

        // 开始模糊匹配
        for (GrantedAuthority pattern : list) {
            if (this.vagueMatch(pattern.getAuthority(), element)) {
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
    public boolean vagueMatch(String pattern, String str) {
        // 两者均为 null 时，直接返回 true
        if (pattern == null && str == null) {
            return true;
        }
        // 两者其一为 null 时，直接返回 false
        if (pattern == null || str == null) {
            return false;
        }
        // 如果表达式不带有*号，则只需简单equals即可 (这样可以使速度提升200倍左右)
        if (!pattern.contains("*")) {
            return pattern.equals(str);
        }
        // 深入匹配
        return vagueMatchMethod(pattern, str);
    }

    /**
     * 字符串模糊匹配
     *
     * @param pattern /
     * @param str     /
     * @return /
     */
    private boolean vagueMatchMethod(String pattern, String str) {
        int m = str.length();
        int n = pattern.length();
        boolean[][] dp = new boolean[m + 1][n + 1];
        dp[0][0] = true;
        for (int i = 1; i <= n; ++i) {
            if (pattern.charAt(i - 1) == '*') {
                dp[0][i] = true;
            }
            else {
                break;
            }
        }
        for (int i = 1; i <= m; ++i) {
            for (int j = 1; j <= n; ++j) {
                if (pattern.charAt(j - 1) == '*') {
                    dp[i][j] = dp[i][j - 1] || dp[i - 1][j];
                }
                else if (str.charAt(i - 1) == pattern.charAt(j - 1)) {
                    dp[i][j] = dp[i - 1][j - 1];
                }
            }
        }
        return dp[m][n];
    }
}
