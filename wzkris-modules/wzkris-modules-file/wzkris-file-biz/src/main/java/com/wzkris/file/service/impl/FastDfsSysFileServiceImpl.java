package com.wzkris.file.service.impl;

import com.github.tobato.fastdfs.domain.fdfs.StorePath;
import com.github.tobato.fastdfs.service.FastFileStorageClient;
import com.wzkris.file.domain.FileChunk;
import com.wzkris.file.service.SysFileService;
import com.wzkris.file.utils.FileUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/**
 * FastDFS 文件存储
 *
 * @author wzkris
 */
@Service
public class FastDfsSysFileServiceImpl implements SysFileService {

    /**
     * 域名或本机访问地址
     */
    @Value("${fdfs.domain}")
    public String domain;

    @Autowired
    private FastFileStorageClient storageClient;

    @Override
    public String uploadFile(MultipartFile file) {
        StorePath storePath = null;
        try {
            storePath = storageClient.uploadFile(file.getInputStream(), file.getSize(),
                    FileUtil.getExtension(file), null);
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage());
        }
        return storePath.getFullPath();
    }

    @Override
    public void sliceUpload(FileChunk fileChunk) {
    }
}
