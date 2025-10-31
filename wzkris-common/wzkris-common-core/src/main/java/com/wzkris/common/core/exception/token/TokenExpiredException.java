package com.wzkris.common.core.exception.token;

import com.wzkris.common.core.exception.BaseException;
import lombok.Getter;

/**
 * @author : wzkris
 * @version : V1.0.0
 * @description : token失效异常
 * @date : 2023/12/15 14:49
 */
@Getter
public final class TokenExpiredException extends BaseException {

    public TokenExpiredException(int biz, String code, Object... args) {
        super("token异常", 401, biz, code, args, null);
    }

}
