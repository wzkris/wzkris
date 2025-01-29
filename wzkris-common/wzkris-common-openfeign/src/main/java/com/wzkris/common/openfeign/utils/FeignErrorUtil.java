package com.wzkris.common.openfeign.utils;

import com.wzkris.common.core.enums.BizCode;
import feign.FeignException;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class FeignErrorUtil {

    /**
     * 非Feign异常则返回RPC异常码
     */
    public static int getCode(Throwable cause) {
        if (cause instanceof FeignException exception) {
            return exception.status();
        }
        return BizCode.RPC_ERROR.value();
    }
}
