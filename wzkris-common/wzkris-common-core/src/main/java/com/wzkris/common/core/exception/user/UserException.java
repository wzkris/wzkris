package com.wzkris.common.core.exception.user;

import com.wzkris.common.core.enums.BizBaseCode;
import com.wzkris.common.core.exception.BaseException;

/**
 * @author : wzkris
 * @version : V1.0.0
 * @description : 用户异常
 * @date : 2023/12/15 14:49
 */
public class UserException extends BaseException {

    public UserException(String code, Object... args) {
        this(BizBaseCode.BAD_REQUEST.value(), code, args);
    }

    public UserException(int biz, String code, Object... args) {
        super("用户异常", biz, code, args, null);
    }

}
