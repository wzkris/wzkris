package com.wzkris.common.core.exception;

import com.wzkris.common.core.utils.I18nUtil;
import org.springframework.lang.Nullable;

/**
 * 基础异常
 *
 * @author wzkris
 */
public class BaseException extends RuntimeException {

    /**
     * 功能模块
     */
    private final String modules;

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
     * 默认异常信息
     */
    private final String defaultMessage;

    public BaseException(String modules, int biz, String code, Object[] args, String defaultMessage) {
        this.modules = modules;
        this.biz = biz;
        this.code = code;
        this.args = args;
        this.defaultMessage = defaultMessage;
    }

    /**
     * 国际化信息
     */
    @Override
    @Nullable
    public String getMessage() {
        if (this.code != null) {
            return I18nUtil.message(this.code, this.args);
        }
        return null;
    }

    public final String getModules() {
        return this.modules;
    }

    public final int getBiz() {
        return this.biz;
    }

    @Nullable
    public String getDefaultMessage() {
        return this.defaultMessage;
    }
}
