package com.wzkris.usercenter.enums.tenantuser;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.lang.Nullable;

@Getter
@AllArgsConstructor
public enum TenantUserStatusEnum {

    ENABLE("0", "启用"),

    DISABLE("1", "禁用");

    @EnumValue
    @JsonValue
    private final String value;

    private final String description;

    @JsonCreator
    @Nullable
    public static TenantUserStatusEnum fromValue(String value) {
        for (TenantUserStatusEnum statusEnum : values()) {
            if (statusEnum.value.equals(value)) {
                return statusEnum;
            }
        }
        return null;
    }

}
