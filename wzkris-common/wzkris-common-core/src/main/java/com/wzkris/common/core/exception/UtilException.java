package com.wzkris.common.core.exception;

import com.wzkris.common.core.enums.BizCode;
import com.wzkris.common.core.exception.base.BaseException;

/**
 * 工具类异常
 *
 * @author wzkris
 */
public final class UtilException extends BaseException {

    public UtilException(String code) {
        this(code, null);
    }

    public UtilException(String code, Object[] args) {
        this(BizCode.FAIL.value(), code, args);
    }

    public UtilException(int biz, String code, Object[] args) {
        super("工具类异常", biz, code, args, null);
    }
}
