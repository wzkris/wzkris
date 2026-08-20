package com.wzkris.common.web.model;

import com.wzkris.common.core.model.Result;

/**
 * api层通用数据处理
 *
 * @author wzkris
 */
public abstract class AbstractApi {

    /**
     * 自定义失败消息
     */
    public static <T> Result<T> resp(int biz, String errMsg) {
        return Result.init(biz, null, errMsg);
    }

    /**
     * 返回成功
     */
    public <T> Result<T> ok() {
        return Result.ok();
    }

    /**
     * 返回成功消息
     */
    public <T> Result<T> ok(T data) {
        return Result.ok(data);
    }

    /**
     * 返回失败消息
     */
    public <T> Result<T> requestFail(String errMsg) {
        return Result.requestFail(errMsg);
    }

    public <T> Result<T> accessDenied(String errMsg) {
        return Result.accessDenied(errMsg);
    }

    /**
     * 响应返回结果
     *
     * @param rows 影响行数
     * @return 操作结果
     */
    protected <T> Result<T> toRes(int rows) {
        return toRes(rows > 0);
    }

    /**
     * 响应返回结果
     *
     * @param result 结果
     * @return 操作结果
     */
    protected <T> Result<T> toRes(boolean result) {
        return result ? ok() : requestFail("操作失败");
    }

}
