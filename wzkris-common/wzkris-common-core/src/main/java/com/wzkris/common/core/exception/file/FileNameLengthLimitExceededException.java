package com.wzkris.common.core.exception.file;

/**
 * 文件名称超长限制异常类
 *
 * @author wzkris
 */
public final class FileNameLengthLimitExceededException extends FileException {
    private static final long serialVersionUID = 1L;

    public FileNameLengthLimitExceededException(int defaultFileNameLength) {
        super("file.name.length.exceed", defaultFileNameLength);
    }
}
