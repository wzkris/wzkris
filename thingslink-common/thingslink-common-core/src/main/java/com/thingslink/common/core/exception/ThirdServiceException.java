package com.thingslink.common.core.exception;

import com.thingslink.common.core.enums.BizCode;
import com.thingslink.common.core.exception.base.BaseException;

/**
 * @author : wzkris
 * @version : V1.0.0
 * @description : 第三方服务异常
 * @date : 2023/5/23 13:30
 */
public final class ThirdServiceException extends BaseException {


    public ThirdServiceException(String code) {
        this(code, null, null);
    }

    public ThirdServiceException(String code, Object... args) {
        this(BizCode.THIRD_SERVICE.value(), code, args, null);
    }

    public ThirdServiceException(int biz, String code, Object... args) {
        super(biz, code, args, null);
    }

}
