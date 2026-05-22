package com.wzkris.gateway.utils;

import com.wzkris.common.core.utils.StringUtil;
import org.springframework.util.AntPathMatcher;

/**
 * Ant 风格路径匹配工具。
 */
public final class PathMatchUtil {

    private static final AntPathMatcher MATCHER = new AntPathMatcher();

    private PathMatchUtil() {
    }

    /**
     * 判断 path 是否匹配 patterns 中任意一条（忽略空白 pattern）。
     */
    public static boolean matchAny(Iterable<String> patterns, String path) {
        if (patterns == null) {
            return false;
        }
        for (String pattern : patterns) {
            if (StringUtil.isNotBlank(pattern) && MATCHER.match(pattern, path)) {
                return true;
            }
        }
        return false;
    }

}
