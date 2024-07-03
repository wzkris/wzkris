package com.wzkris.common.core.exception.param;

import com.wzkris.common.core.enums.BizCode;
import com.wzkris.common.core.exception.base.BaseException;

/**
 * 验证码错误异常类
 *
 * @author wzkris
 */
public class CaptchaException extends BaseException {


    public CaptchaException(String code) {
        super(BizCode.PRECONDITION_FAILED.value(), code, null, null);
    }

    public CaptchaException(String code, Object... args) {
        super(BizCode.PRECONDITION_FAILED.value(), code, args, null);
    }

    public CaptchaException(int biz, String code, Object... args) {
        super(biz, code, args, null);
    }
}
