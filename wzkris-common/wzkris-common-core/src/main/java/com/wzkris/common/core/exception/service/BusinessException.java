package com.wzkris.common.core.exception.service;

import com.wzkris.common.core.enums.BizCode;
import com.wzkris.common.core.exception.BaseException;

/**
 * @author : wzkris
 * @version : V1.0.0
 * @description : 业务异常
 * @date : 2023/12/27 11:23
 */
public final class BusinessException extends BaseException {

    public BusinessException(String code, Object... args) {
        this(BizCode.INVOKE_FAIL.value(), code, args);
    }

    public BusinessException(int biz, String code, Object... args) {
        super("业务异常", biz, code, args, null);
    }
}
