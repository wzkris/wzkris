package com.thingslink.common.core.exception.file;

/**
 * 文件名大小限制异常类
 *
 * @author wzkris
 */
public final class FileSizeLimitExceededException extends FileException {
    private static final long serialVersionUID = 1L;

    public FileSizeLimitExceededException(long defaultMaxSize) {
        super("file.size.exceed", defaultMaxSize);
    }
}
