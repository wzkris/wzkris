package com.wzkris.principal.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import org.springframework.lang.Nullable;

/**
 * 钱包明细类型
 */
@AllArgsConstructor
public enum WalletRecordTypeEnum {
    /**
     * 收入
     */
    INCOME("0"),
    /**
     * 支出
     */
    OUTCOME("1");

    private final String value;

    @JsonCreator
    @Nullable
    public static WalletRecordTypeEnum fromValue(String value) {
        for (WalletRecordTypeEnum typeEnum : values()) {
            if (typeEnum.value.equals(value)) {
                return typeEnum;
            }
        }
        return null;
    }

    @JsonValue
    public String getValue() {
        return value;
    }
}
