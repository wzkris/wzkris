package com.wzkris.common.core.exception.file;

import com.wzkris.common.core.enums.BizCode;
import com.wzkris.common.core.exception.BaseException;

/**
 * 文件信息异常类
 *
 * @author wzkris
 */
public class FileException extends BaseException {

    public FileException(String code, Object... args) {
        this(BizCode.INVOKE_FAIL.value(), code, args);
    }

    public FileException(int biz, String code, Object... args) {
        super("文件异常", biz, code, args, null);
    }

}
