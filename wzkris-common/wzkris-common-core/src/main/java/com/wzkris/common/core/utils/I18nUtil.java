package com.wzkris.common.core.utils;

import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;

/**
 * 获取i18n资源文件
 *
 * @author Lion Li
 */
@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class I18nUtil {

    static final Pattern pattern = Pattern.compile("\\{([^}]*)}");

    private static final MessageSource MESSAGE_SOURCE = SpringUtil.getFactory().getBean(MessageSource.class);

    /**
     * 根据消息键和参数 获取消息 委托给spring messageSource
     *
     * @param code 消息键
     * @param args 参数
     * @return 获取国际化翻译值
     */
    public static String message(String code, Object... args) {
        return MESSAGE_SOURCE.getMessage(code, args, LocaleContextHolder.getLocale());
    }

    public static String messageRegex(String patternCode) {
        Matcher matcher = pattern.matcher(patternCode);
        Locale locale = LocaleContextHolder.getLocale();
        StringBuilder msg = new StringBuilder();
        // 查找所有匹配并添加到列表中
        while (matcher.find()) {
            msg.append(MESSAGE_SOURCE.getMessage(matcher.group(1), null, locale));
        }
        return msg.toString();
    }
}
