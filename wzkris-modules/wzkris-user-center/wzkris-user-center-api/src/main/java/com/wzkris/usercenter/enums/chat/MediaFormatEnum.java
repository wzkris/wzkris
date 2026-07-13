package com.wzkris.usercenter.enums.chat;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.lang.Nullable;

import java.util.Locale;

@Getter
@AllArgsConstructor
public enum MediaFormatEnum {

    TEXT("text"),
    PNG("png"),
    JPG("jpg"),
    JPEG("jpeg"),
    GIF("gif"),
    WEBP("webp"),
    BMP("bmp"),
    MP4("mp4"),
    WEBM("webm"),
    PDF("pdf"),
    SVG("svg"),
    TXT("txt"),
    UNKNOWN("unknown");

    @EnumValue
    @JsonValue
    private final String value;

    public static MediaFormatEnum fromProtocol(@Nullable String raw) {
        if (raw == null || raw.isBlank()) {
            return UNKNOWN;
        }
        String key = raw.trim().toLowerCase(Locale.ROOT);
        for (MediaFormatEnum e : values()) {
            if (e.value.equals(key)) {
                return e;
            }
        }
        return UNKNOWN;
    }

    @JsonCreator
    @Nullable
    public static MediaFormatEnum fromValue(String value) {
        if (value == null) {
            return null;
        }
        for (MediaFormatEnum e : values()) {
            if (e.value.equalsIgnoreCase(value)) {
                return e;
            }
        }
        return null;
    }
}
