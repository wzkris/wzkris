package com.wzkris.common.core.exception.captcha;

import com.wzkris.common.core.enums.BizCode;
import com.wzkris.common.core.exception.BaseException;

/**
 * 验证码错误异常类
 *
 * @author wzkris
 */
public class CaptchaException extends BaseException {

    public CaptchaException(String code, Object... args) {
        this(BizCode.PRECONDITION_FAILED.value(), code, args);
    }

    public CaptchaException(int biz, String code, Object... args) {
        super("验证码异常", biz, code, args, null);
    }

}
