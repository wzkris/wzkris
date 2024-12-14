package com.wzkris.common.core.exception;

import com.wzkris.common.core.enums.BizCode;
import com.wzkris.common.core.exception.base.BaseException;

/**
 * @author : wzkris
 * @version : V1.0.0
 * @description : 业务异常，国际化
 * @date : 2023/12/27 11:23
 */
public final class BusinessExceptionI18n extends BaseException {

    public BusinessExceptionI18n(int biz, String code, Object... args) {
        super("业务异常", biz, code, args, null);
    }

    // 指向国际化信息
    public BusinessExceptionI18n(String code, Object... args) {
        this(BizCode.FAIL.value(), code, args);
    }

    @Override
    public String getDefaultMessage() {
        return this.getMessage();
    }
}
