package com.wzkris.common.core.exception.service;

import com.wzkris.common.core.exception.BaseException;

/**
 * @author : wzkris
 * @version : V1.0.0
 * @description : 业务异常
 * @date : 2023/12/27 11:23
 */
public final class BusinessException extends BaseException {

    public BusinessException(int biz, String message) {
        super("业务异常", 200, biz, null, null, message);
    }

    @Override
    public Throwable fillInStackTrace() {
        return this;
    }

}
