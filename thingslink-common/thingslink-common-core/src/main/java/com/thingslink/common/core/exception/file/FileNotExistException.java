package com.thingslink.common.core.exception.file;

/**
 * @author : wzkris
 * @version : V1.0.0
 * @description : 文件不存在异常
 * @date : 2023/5/23 16:24
 */
public final class FileNotExistException extends FileException{
    private static final long serialVersionUID = -4515447597543303483L;

    public FileNotExistException(String fileName) {
        super("file.not.exist", fileName);
    }
}
