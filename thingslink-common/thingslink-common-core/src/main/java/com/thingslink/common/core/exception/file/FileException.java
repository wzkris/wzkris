package com.thingslink.common.core.exception.file;

import com.thingslink.common.core.enums.BizCode;
import com.thingslink.common.core.exception.base.BaseException;

/**
 * 文件信息异常类
 *
 * @author wzkris
 */
public class FileException extends BaseException {
    private static final long serialVersionUID = 1L;

    public FileException(String code) {
        this(code, null, null);
    }

    public FileException(String code, Object... args) {
        this(BizCode.FAIL.value(), code, args, null);
    }
    public FileException(int biz, String code, Object... args) {
        super(biz, code, args, null);
    }

}
