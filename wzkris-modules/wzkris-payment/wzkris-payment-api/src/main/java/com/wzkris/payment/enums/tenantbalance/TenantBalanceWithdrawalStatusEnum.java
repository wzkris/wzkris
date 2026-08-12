package com.wzkris.payment.enums.tenantbalance;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.lang.Nullable;

@Getter
@AllArgsConstructor
public enum TenantBalanceWithdrawalStatusEnum {

    PROCESSING("PROCESSING", "处理中"),

    SUCCESS("SUCCESS", "成功"),

    FAILED("FAILED", "失败");

    @EnumValue
    @JsonValue
    private final String value;

    private final String description;

    @JsonCreator
    @Nullable
    public static TenantBalanceWithdrawalStatusEnum fromValue(String value) {
        for (TenantBalanceWithdrawalStatusEnum statusEnum : values()) {
            if (statusEnum.value.equals(value)) {
                return statusEnum;
            }
        }
        return null;
    }

}