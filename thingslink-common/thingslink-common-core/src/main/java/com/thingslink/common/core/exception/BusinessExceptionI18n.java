package com.thingslink.common.core.exception;

import com.thingslink.common.core.enums.BizCode;

/**
 * @author : wzkris
 * @version : V1.0.0
 * @description : 业务异常，国际化
 * @date : 2023/12/27 11:23
 */
public final class BusinessExceptionI18n extends BusinessException {

    // 国际化信息 带参数
    public BusinessExceptionI18n(int biz, String code, Object... args) {
        super(biz, code, args);
    }

    public BusinessExceptionI18n(String code, Object... args) {
        this(BizCode.FAIL.value(), code, args);
    }
}
