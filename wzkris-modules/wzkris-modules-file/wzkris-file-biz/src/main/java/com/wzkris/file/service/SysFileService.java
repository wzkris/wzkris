package com.wzkris.file.service;

import com.wzkris.file.domain.FileChunk;
import org.springframework.web.multipart.MultipartFile;

/**
 * 文件上传接口
 *
 * @author wzkris
 */
public interface SysFileService {

    /**
     * 文件上传接口
     *
     * @param file 上传的文件
     * @return 访问地址
     */
    String uploadFile(MultipartFile file);

    /**
     * 文件上传接口
     *
     * @param fileChunk 分片文件
     */
    void sliceUpload(FileChunk fileChunk);
}
