package com.wzkris.common.core.exception;

import com.wzkris.common.core.enums.BizCode;
import com.wzkris.common.core.exception.base.BaseException;

/**
 * 业务异常
 *
 * @author wzkris
 */
public final class BusinessException extends BaseException {

    // 自定义业务代码及错误信息
    public BusinessException(int biz, String message) {
        super("业务异常", biz, null, null, message);
    }

    public BusinessException(BizCode bizCode) {
        this(bizCode.value(), bizCode.desc());
    }

    public BusinessException(String message) {
        this(BizCode.INVOKE_FAIL.value(), message);
    }

    /**
     * @return 返回自定义错误信息
     */
    @Override
    public String getMessage() {
        return this.getDefaultMessage();
    }
}
