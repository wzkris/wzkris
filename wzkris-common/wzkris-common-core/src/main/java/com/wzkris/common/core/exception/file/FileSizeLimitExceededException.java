package com.wzkris.common.core.exception.file;

import com.wzkris.common.core.enums.BizCode;

/**
 * 文件大小异常类
 *
 * @author wzkris
 */
public final class FileSizeLimitExceededException extends FileException {

    public FileSizeLimitExceededException(long defaultMaxSize) {
        super("文件大小异常", BizCode.BAD_REQUEST.value(), "file.size.exceed", new Object[]{defaultMaxSize});
    }
}
