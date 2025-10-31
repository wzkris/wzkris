package com.wzkris.common.core.exception.service;

import com.wzkris.common.core.enums.BizBaseCode;
import com.wzkris.common.core.exception.BaseException;

/**
 * 通用异常
 *
 * @author wzkris
 */
public final class GenericException extends BaseException {

    public GenericException(int biz, String message) {
        super("通用异常", 500, biz, null, null, message);
    }

    public GenericException(String message) {
        this(BizBaseCode.REQUEST_ERROR.value(), message);
    }

    /**
     * @return 返回自定义错误信息
     */
    @Override
    public String getMessage() {
        return this.getDefaultMessage();
    }

}
