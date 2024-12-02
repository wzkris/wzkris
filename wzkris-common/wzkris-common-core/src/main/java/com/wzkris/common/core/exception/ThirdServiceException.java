package com.wzkris.common.core.exception;

import com.wzkris.common.core.enums.BizCode;
import com.wzkris.common.core.exception.base.BaseException;

/**
 * @author : wzkris
 * @version : V1.0.0
 * @description : 第三方服务异常
 * @date : 2023/5/23 13:30
 */
public final class ThirdServiceException extends BaseException {

    // 自定义业务代码及错误信息
    public ThirdServiceException(int biz, String message) {
        super("第三方服务异常", biz, null, null, message);
    }

    public ThirdServiceException(BizCode bizCode) {
        this(bizCode.value(), bizCode.desc());
    }

    public ThirdServiceException(String message) {
        this(BizCode.THIRD_SERVICE.value(), message);
    }

    /**
     * @return 返回自定义错误信息
     */
    @Override
    public String getMessage() {
        return this.getDefaultMessage();
    }
}
