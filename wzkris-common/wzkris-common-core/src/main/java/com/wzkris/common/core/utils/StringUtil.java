package com.wzkris.common.core.utils;

import com.wzkris.common.core.constant.CommonConstants;
import org.apache.commons.lang3.StringUtils;

/**
 * 字符串工具类
 *
 * @author wzkris
 */
public abstract class StringUtil extends StringUtils {

    /**
     * 点
     */
    public static final String DOT = ".";

    /**
     * 逗号
     */
    public static final CharSequence COMMA = ",";

    /**
     * 下划线
     */
    public static final char SEPARATOR = '_';

    /**
     * 井号
     */
    public static final String HASH = "#";

    /**
     * @
     */
    public static final String AT = "@";

    /**
     * 去空格
     */
    public static String trim(String str) {
        return (str == null ? "" : str.trim());
    }

    /**
     * 是否为http(s)://开头
     *
     * @param link 链接
     * @return 结果
     */
    public static boolean ishttp(String link) {
        return StringUtil.startsWith(link, CommonConstants.HTTP) || StringUtil.startsWith(link, CommonConstants.HTTPS);
    }

    /**
     * 基于 Unicode 代码点替换字符串中的指定区域
     *
     * @param str          原始字符串
     * @param startInclude 起始代码点索引（包含）
     * @param endExclude   结束代码点索引（不包含）
     * @param replacedChar 用于替换的字符
     * @return 替换后的新字符串
     */
    public static String replaceAt(String str, int startInclude, int endExclude, char replacedChar) {
        if (isEmpty(str)) {
            return StringUtils.defaultString(str);
        }

        int[] strCodePoints = str.codePoints().toArray();
        final int strLength = strCodePoints.length;

        if (startInclude > strLength) {
            return str;
        }
        if (endExclude > strLength) {
            endExclude = strLength;
        }
        if (startInclude > endExclude) {
            return str;
        }

        for (int i = startInclude; i < endExclude; i++) {
            strCodePoints[i] = replacedChar;
        }

        return new String(strCodePoints, 0, strCodePoints.length);
    }

    /**
     * 驼峰转下划线命名
     */
    public static String toUnderScoreCase(String str) {
        if (str == null) {
            return null;
        }
        StringBuilder sb = new StringBuilder();
        // 前置字符是否大写
        boolean preCharIsUpperCase;
        // 当前字符是否大写
        boolean curreCharIsUpperCase;
        // 下一字符是否大写
        boolean nexteCharIsUpperCase = true;
        for (int i = 0; i < str.length(); i++) {
            char c = str.charAt(i);
            if (i > 0) {
                preCharIsUpperCase = Character.isUpperCase(str.charAt(i - 1));
            } else {
                preCharIsUpperCase = false;
            }

            curreCharIsUpperCase = Character.isUpperCase(c);

            if (i < (str.length() - 1)) {
                nexteCharIsUpperCase = Character.isUpperCase(str.charAt(i + 1));
            }

            if (preCharIsUpperCase && curreCharIsUpperCase && !nexteCharIsUpperCase) {
                sb.append(SEPARATOR);
            } else if ((i != 0 && !preCharIsUpperCase) && curreCharIsUpperCase) {
                sb.append(SEPARATOR);
            }
            sb.append(Character.toLowerCase(c));
        }

        return sb.toString();
    }

    /**
     * 将下划线大写方式命名的字符串转换为驼峰式。如果转换前的下划线大写方式命名的字符串为空，则返回空字符串。 例如：HELLO_WORLD->HelloWorld
     *
     * @param name 转换前的下划线大写方式命名的字符串
     * @return 转换后的驼峰式命名的字符串
     */
    public static String convertToCamelCase(String name) {
        StringBuilder result = new StringBuilder();
        // 快速检查
        if (name == null || name.isEmpty()) {
            // 没必要转换
            return "";
        } else if (!name.contains("_")) {
            // 不含下划线，仅将首字母大写
            return name.substring(0, 1).toUpperCase() + name.substring(1);
        }
        // 用下划线将原始字符串分割
        String[] camels = name.split("_");
        for (String camel : camels) {
            // 跳过原始字符串中开头、结尾的下换线或双重下划线
            if (camel.isEmpty()) {
                continue;
            }
            // 首字母大写
            result.append(camel.substring(0, 1).toUpperCase());
            result.append(camel.substring(1).toLowerCase());
        }
        return result.toString();
    }

    /**
     * 驼峰式命名法 例如：username->username
     */
    public static String toCamelCase(String s) {
        if (s == null) {
            return null;
        }
        s = s.toLowerCase();
        StringBuilder sb = new StringBuilder(s.length());
        boolean upperCase = false;
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);

            if (c == SEPARATOR) {
                upperCase = true;
            } else if (upperCase) {
                sb.append(Character.toUpperCase(c));
                upperCase = false;
            } else {
                sb.append(c);
            }
        }
        return sb.toString();
    }

    /**
     * 调用ToString方法
     *
     * @param o 对象
     * @return 字符串
     */
    public static String toStringOrNull(Object o) {
        if (o == null) {
            return null;
        }
        return o.toString();
    }

}
