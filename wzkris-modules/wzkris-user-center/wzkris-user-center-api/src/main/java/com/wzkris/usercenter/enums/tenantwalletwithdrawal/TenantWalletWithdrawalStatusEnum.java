package com.wzkris.usercenter.enums.tenantwalletwithdrawal;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.lang.Nullable;

@Getter
@AllArgsConstructor
public enum TenantWalletWithdrawalStatusEnum {

    PROCESSING("0", "处理中"),

    SUCCESS("1", "成功"),

    FAIL("2", "失败");

    @EnumValue
    @JsonValue
    private final String value;

    private final String description;

    @JsonCreator
    @Nullable
    public static TenantWalletWithdrawalStatusEnum fromValue(String value) {
        for (TenantWalletWithdrawalStatusEnum statusEnum : values()) {
            if (statusEnum.value.equals(value)) {
                return statusEnum;
            }
        }
        return null;
    }

}
