package com.wzkris.common.core.exception.file;

import com.wzkris.common.core.enums.BizCode;

/**
 * 文件名称超长限制异常类
 *
 * @author wzkris
 */
public final class FileNameLengthLimitExceededException extends FileException {

    public FileNameLengthLimitExceededException(int defaultFileNameLength) {
        super("文件名称长度异常", BizCode.BAD_REQUEST.value(), "file.name.length.exceed", new Object[]{defaultFileNameLength});
    }
}
