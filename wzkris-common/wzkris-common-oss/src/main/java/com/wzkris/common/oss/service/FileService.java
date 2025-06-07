package com.wzkris.common.oss.service;

import com.wzkris.common.oss.domain.FileVO;
import java.io.InputStream;

/**
 * 文件接口
 *
 * @author wzkris
 */
public interface FileService {

    /**
     * 支持类型
     */
    String support();

    /**
     * @param is           文件流
     * @param relativePath 存储的相对地址
     * @param fileName     文件名
     * @param contentType  控制浏览器
     * @return
     */
    FileVO upload(InputStream is, String relativePath, String fileName, String contentType);
}
