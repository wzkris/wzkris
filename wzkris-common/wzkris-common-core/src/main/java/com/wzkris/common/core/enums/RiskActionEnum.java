package com.wzkris.common.core.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.lang.Nullable;

/**
 * 风控动作
 */
@Getter
@AllArgsConstructor
public enum RiskActionEnum {

    SHOW_CAPTCHA_MODAL("SHOW_CAPTCHA_MODAL", ""),

    SHOW_BLOCK_MODAL("SHOW_BLOCK_MODAL", ""),
    ;

    @JsonValue
    private final String value;

    private final String description;

    @JsonCreator
    @Nullable
    public static RiskActionEnum fromValue(String value) {
        for (RiskActionEnum actionEnum : values()) {
            if (actionEnum.value.equals(value)) {
                return actionEnum;
            }
        }
        return null;
    }
}
