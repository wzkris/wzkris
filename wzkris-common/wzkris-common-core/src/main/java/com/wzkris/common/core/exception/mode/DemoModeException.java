package com.wzkris.common.core.exception.mode;

import com.wzkris.common.core.enums.BizBaseCodeEnum;
import com.wzkris.common.core.exception.BaseException;

/**
 * 演示模式异常
 *
 * @author wzkris
 */
public final class DemoModeException extends BaseException {

    public DemoModeException() {
        super("演示异常", 403, BizBaseCodeEnum.ACCESS_DENIED.value(), null, null, "演示模式，不允许操作");
    }

    @Override
    public Throwable fillInStackTrace() {
        return this;
    }

}
