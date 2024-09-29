package com.wzkris.common.core.exception.file;

import com.wzkris.common.core.enums.BizCode;
import com.wzkris.common.core.exception.base.BaseException;

/**
 * 文件信息异常类
 *
 * @author wzkris
 */
public class FileException extends BaseException {

    public FileException(String code, Object[] args) {
        this(BizCode.FAIL.value(), code, args);
    }

    public FileException(int biz, String code, Object[] args) {
        this("文件异常", biz, code, args);
    }

    public FileException(String modules, int biz, String code, Object[] args) {
        super(modules, biz, code, args, null);
    }

}
