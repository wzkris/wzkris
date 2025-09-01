package com.wzkris.common.core.exception.service;

import com.wzkris.common.core.exception.BaseException;

/**
 * @author : wzkris
 * @version : V1.0.0
 * @description : 外部服务异常
 * @date : 2023/5/23 13:30
 */
public final class ExternalServiceException extends BaseException {

    public ExternalServiceException(int biz, String message) {
        super("外部服务异常", biz, null, null, message);
    }

    /**
     * @return 返回自定义错误信息
     */
    @Override
    public String getMessage() {
        return this.getDefaultMessage();
    }

}
