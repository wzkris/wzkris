package com.wzkris.common.core.utils;

import com.wzkris.common.core.model.Result;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public abstract class ResultUtil {

    public static <T> boolean check(Result<T> r) {
        return r.isSuccess() && r.getData() != null;
    }

    public static <T> boolean checkNoData(Result<T> r) {
        return r.isSuccess();
    }

    public static <T> Result<T> failed(int code, String message) {
        return Result.init(code, null, message);
    }

    public static <T> Result<T> failed(Result<?> r) {
        return Result.init(r.getCode(), null, r.getMessage());
    }

}
