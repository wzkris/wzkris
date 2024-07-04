package com.wzkris.common.core.exception.user;

import com.wzkris.common.core.enums.BizCode;
import com.wzkris.common.core.exception.base.BaseException;

/**
 * @author : wzkris
 * @version : V1.0.0
 * @description : 用户异常
 * @date : 2023/12/15 14:49
 */
public class UserException extends BaseException {

    public UserException(String code) {
        this(code, null, null);
    }

    public UserException(String code, Object... args) {
        this(BizCode.FAIL.value(), code, args, null);
    }

    public UserException(int biz, String code, Object... args) {
        super(biz, code, args, null);
    }
}
