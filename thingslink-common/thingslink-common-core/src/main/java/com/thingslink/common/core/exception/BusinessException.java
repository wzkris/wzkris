package com.thingslink.common.core.exception;

import com.thingslink.common.core.enums.BizCode;
import com.thingslink.common.core.exception.base.BaseException;

/**
 * 业务异常
 *
 * @author wzkris
 */
public class BusinessException extends BaseException {
    

    public BusinessException(BizCode bizCode) {
        super(bizCode.value(), null, null, bizCode.desc());
    }

    // 自定义业务代码及错误信息
    public BusinessException(int biz, String message) {
        super(biz, null, null, message);
    }

    // 自定义信息
    public BusinessException(String message) {
        super(BizCode.FAIL.value(), null, null, message);
    }

    // 指向国际化信息
    protected BusinessException(String code, Object[] args) {
        this(BizCode.FAIL.value(), code, args);
    }

    protected BusinessException(int biz, String code, Object[] args) {
        super(biz, code, args, null);
    }
}
