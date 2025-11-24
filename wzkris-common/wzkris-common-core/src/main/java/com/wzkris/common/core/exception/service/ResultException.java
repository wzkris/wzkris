package com.wzkris.common.core.exception.service;

import com.wzkris.common.core.exception.BaseException;
import com.wzkris.common.core.model.Result;
import lombok.Getter;

@Getter
public class ResultException extends BaseException {

    private final Result result;

    public ResultException(int httpStatusCode, Result result) {
        super("result结果异常", httpStatusCode, result.getCode(), null, null, result.getMessage());
        this.result = result;
    }

    public ResultException(int httpStatusCode, int biz, String msg) {
        super("result结果异常", httpStatusCode, biz, null, null, msg);
        this.result = Result.init(biz, null, msg);
    }

    /**
     * @return 返回自定义错误信息
     */
    @Override
    public String getMessage() {
        return this.getDefaultMessage();
    }

}
