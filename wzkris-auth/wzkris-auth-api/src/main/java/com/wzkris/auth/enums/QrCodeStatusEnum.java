package com.wzkris.auth.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.lang.Nullable;

@Getter
@AllArgsConstructor
public enum QrCodeStatusEnum {
    OVERDUE("-1", "过期"),
    WAIT("1", "等待扫描"),
    SCANED("2", "已扫描,未确认"),
    CONFIRM("3", "已确认");

    @JsonValue
    private final String value;

    private final String description;

    @JsonCreator
    @Nullable
    public static QrCodeStatusEnum fromValue(String value) {
        for (QrCodeStatusEnum status : values()) {
            if (status.value.equals(value)) {
                return status;
            }
        }
        return null;
    }
}
