package com.wzkris.common.core.exception.file;

import com.wzkris.common.core.enums.BizCode;

/**
 * @author : wzkris
 * @version : V1.0.0
 * @description : 文件不存在异常
 * @date : 2023/5/23 16:24
 */
public final class FileNotExistException extends FileException {

    public FileNotExistException(String fileName) {
        super("文件不存在异常", BizCode.BAD_REQUEST.value(), "file.not.exist", new Object[]{fileName});
    }
}
