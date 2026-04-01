package com.wzkris.common.core.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import org.springframework.lang.Nullable;

/**
 * 风险等级
 */
@AllArgsConstructor
public enum RiskLevelEnum {

    LOW("LOW"),

    MEDIUM("MEDIUM"),

    HIGH("HIGH");

    private final String value;

    @JsonCreator
    @Nullable
    public static RiskLevelEnum fromValue(String value) {
        for (RiskLevelEnum levelEnum : values()) {
            if (levelEnum.value.equals(value)) {
                return levelEnum;
            }
        }
        return null;
    }

    @JsonValue
    public String getValue() {
        return value;
    }

}
