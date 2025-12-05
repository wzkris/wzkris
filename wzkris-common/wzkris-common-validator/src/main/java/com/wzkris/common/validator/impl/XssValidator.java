package com.wzkris.common.validator.impl;

import com.wzkris.common.validator.annotation.Xss;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 自定义xss校验注解实现
 *
 * @author wzkris
 */
public class XssValidator implements ConstraintValidator<Xss, String> {

    /**
     * HTML标签匹配模式
     * 匹配：<tag>、<tag/>、<tag />、<tag attr="value">等格式
     * 支持多行匹配（DOTALL模式）
     */
    private static final Pattern HTML_ENTITY_PATTERN = Pattern.compile(
            // 匹配实际的HTML开始标签
            "<(?!/?[!?])[a-zA-Z][^>]*>" +
                    // 匹配HTML结束标签
                    "|</[a-zA-Z][^>]*>",
            Pattern.CASE_INSENSITIVE
    );

    /**
     * 检查字符串中是否包含HTML标签
     *
     * @param value 待检查的字符串
     * @return true表示包含HTML标签，false表示不包含
     */
    public static boolean containsHtml(String value) {
        if (value == null || value.isEmpty()) {
            return false;
        }
        Matcher matcher = HTML_ENTITY_PATTERN.matcher(value);
        return matcher.find();
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext constraintValidatorContext) {
        if (value == null) {
            return true;
        }
        return !containsHtml(value);
    }

}
