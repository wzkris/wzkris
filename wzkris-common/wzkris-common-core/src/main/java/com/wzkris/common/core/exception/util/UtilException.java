package com.wzkris.common.core.exception.util;

import com.wzkris.common.core.enums.BizCode;
import com.wzkris.common.core.exception.BaseException;

/**
 * 工具类异常
 *
 * @author wzkris
 */
public final class UtilException extends BaseException {

    public UtilException(String code, Object... args) {
        this(BizCode.INTERNAL_ERROR.value(), code, args);
    }

    public UtilException(int biz, String code, Object... args) {
        super("工具类异常", biz, code, args, null);
    }

}
