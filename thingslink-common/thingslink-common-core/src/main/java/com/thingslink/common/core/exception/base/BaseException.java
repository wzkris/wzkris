package com.thingslink.common.core.exception.base;

import com.thingslink.common.core.enums.BizCode;
import com.thingslink.common.core.utils.MessageUtil;

/**
 * 基础异常
 *
 * @author wzkris
 */
public class BaseException extends RuntimeException {
    
    /**
     * 状态码
     */
    private final int biz;
    /**
     * 错误码，返回前端国际化信息
     */
    private final String code;
    /**
     * 错误码对应的参数
     */
    private final Object[] args;
    /**
     * 异常信息；此异常信息仅为系统日志打印，不会返回给前端，若要返回内容给前端请走国际化
     */
    private final String defaultMessage;

    public BaseException(int biz, String code, Object[] args, String defaultMessage) {
        this.biz = biz;
        this.code = code;
        this.args = args;
        this.defaultMessage = defaultMessage;
    }

    public BaseException(String code, Object[] args) {
        this(BizCode.FAIL.value(), code, args, null);
    }

    /**
     * 该构造器生成的异常信息不会被返回给前端
     */
    public BaseException(String defaultMessage) {
        this(BizCode.FAIL.value(), null, null, defaultMessage);
    }

    /**
     * 获取返回信息
     */
    @Override
    public String getMessage() {
        if (this.code != null) {
            return MessageUtil.message(this.code, this.args);
        }
        return this.defaultMessage;
    }

    public int getBiz() {
        return this.biz;
    }
}
