package com.wzkris.auth.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import org.springframework.lang.Nullable;

@AllArgsConstructor
public enum QrCodeStatus {
    /**
     * 过期
     */
    OVERDUE("-1"),
    /**
     * 等待扫描
     */
    WAIT("1"),
    /**
     * 已扫描 未确认
     */
    SCANED("2"),
    /**
     * 已确认
     */
    CONFIRM("3");

    private final String value;

    @JsonCreator
    @Nullable
    public static QrCodeStatus fromValue(String value) {
        for (QrCodeStatus status : values()) {
            if (status.value.equals(value)) {
                return status;
            }
        }
        return null;
    }

    @JsonValue
    public String getValue() {
        return value;
    }
}
