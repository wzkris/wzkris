package com.wzkris.common.core.exception.request;

import com.wzkris.common.core.enums.BizBaseCode;
import com.wzkris.common.core.exception.BaseException;

/**
 * @author : wzkris
 * @version : V1.0.0
 * @description : 限流异常
 * @date : 2025/02/17 09:01
 */
public final class TooManyRequestException extends BaseException {

    public TooManyRequestException() {
        this(BizBaseCode.TOO_MANY_REQUESTS.value(), "service.internalError.busy");
    }

    public TooManyRequestException(int biz, String code, Object... args) {
        super("限流异常", 429, biz, code, args, null);
    }

}
