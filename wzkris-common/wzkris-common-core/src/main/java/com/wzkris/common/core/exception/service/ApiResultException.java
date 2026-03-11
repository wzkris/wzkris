package com.wzkris.common.core.exception.service;

import com.wzkris.common.core.exception.BaseException;
import com.wzkris.common.core.model.Result;
import com.wzkris.common.core.utils.JsonUtil;
import lombok.Getter;

@Getter
public class ApiResultException extends BaseException {

    private final Result result;

    public ApiResultException(int httpStatusCode, Result result) {
        super("api result结果异常", httpStatusCode, result.getCode(), null, null, result.getMessage());
        this.result = result;
    }

    /**
     * @return 返回自定义错误信息
     */
    @Override
    public String getMessage() {
        return JsonUtil.toJsonString(result);
    }

}
