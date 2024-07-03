package com.wzkris.common.core.exception;

import com.wzkris.common.core.enums.BizCode;
import com.wzkris.common.core.exception.base.BaseException;

import java.io.Serial;

/**
 * 工具类异常
 *
 * @author wzkris
 */
public final class UtilException extends BaseException {
    @Serial
    private static final long serialVersionUID = 8247610319171014183L;

    public UtilException(String code) {
        this(code, null, null);
    }

    public UtilException(String code, Object... args) {
        this(BizCode.FAIL.value(), code, args, null);
    }

    public UtilException(int biz, String code, Object... args) {
        super(biz, code, args, null);
    }
}
