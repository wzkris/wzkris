package com.wzkris.common.core.exception.service;

import com.wzkris.common.core.enums.BizCode;
import com.wzkris.common.core.exception.BaseException;

/**
 * @author : wzkris
 * @version : V1.0.0
 * @description : 第三方服务异常
 * @date : 2023/5/23 13:30
 */
public final class ThirdServiceException extends BaseException {

    public ThirdServiceException(int biz, String message) {
        super("第三方服务异常", biz, null, null, message);
    }

    public ThirdServiceException(String message) {
        this(BizCode.THIRD_SERVICE.value(), message);
    }

    /**
     * @return 返回自定义错误信息
     */
    @Override
    public String getMessage() {
        return this.getDefaultMessage();
    }
}
