package com.wzkris.usercenter.enums.wallet;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.lang.Nullable;

/**
 * 钱包明细类型
 */
@Getter
@AllArgsConstructor
public enum WalletRecordTypeEnum {
    /**
     * 收入
     */
    INCOME("0", "收入"),
    /**
     * 支出
     */
    OUTCOME("1", "支出");

    @EnumValue
    @JsonValue
    private final String value;

    private final String description;

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

}
