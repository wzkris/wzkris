package com.wzkris.common.notifier.enums;

import lombok.AllArgsConstructor;
import org.springframework.util.StringUtils;

import java.util.Map;
import java.util.Set;

/**
 * 钉钉消息模板Key枚举
 *
 * @author wzkris
 * @date 2025/11/06
 */
@AllArgsConstructor
public enum DingtalkTemplateKey {
    TEXT(
            "sampleText",
            Set.of("content"),
            Set.of("content")
    ),
    MARKDOWN(
            "sampleMarkdown",
            Set.of("title", "text"),
            Set.of("title", "text")
    ),
    IMAGE(
            "sampleImageMsg",
            Set.of("photoURL"),
            Set.of("photoURL")
    ),
    LINK(
            "sampleLink",
            Set.of("text", "title", "picUrl", "messageUrl"),
            Set.of("text", "title", "picUrl", "messageUrl")
    ),
    ACTION_CARD(
            "sampleActionCard",
            Set.of("title", "text", "singleTitle", "singleURL"),
            Set.of("title", "text", "singleTitle", "singleURL")
    ),
    AUDIO(
            "sampleAudio",
            Set.of("mediaId", "duration"),
            Set.of("mediaId", "duration")
    ),
    FILE(
            "sampleFile",
            Set.of("mediaId", "fileName", "fileType"),
            Set.of("mediaId", "fileName", "fileType")
    ),
    VIDEO(
            "sampleVideo",
            Set.of("duration", "videoMediaId", "videoType", "picMediaId"),
            Set.of("duration", "videoMediaId", "videoType", "picMediaId")
    );

    private final String value;

    // 必须出现的字段
    private final Set<String> requiredKeys;

    // 允许出现的字段
    private final Set<String> allowedKeys;

    public final String value() {
        return value;
    }

    public void validate(Map<String, Object> params) {
        if (params == null) {
            throw new IllegalArgumentException(String.format("模板‘%s’的参数不能为空", value));
        }
        for (String key : requiredKeys) {
            Object v = params.get(key);
            if (v == null) {
                throw new IllegalArgumentException(String.format("模板‘%s’缺少必填参数'%s'", value, key));
            }
            if (v instanceof String && !StringUtils.hasText((String) v)) {
                throw new IllegalArgumentException(String.format("模板‘%s’参数'%s'不能为空字符串", value, key));
            }
        }
        for (String key : params.keySet()) {
            if (!allowedKeys.contains(key)) {
                throw new IllegalArgumentException(String.format("模板‘%s’存在不支持的参数'%s'", value, key));
            }
        }
    }
}


