package com.wzkris.common.oss.service.impl;

import com.github.tobato.fastdfs.domain.fdfs.StorePath;
import com.github.tobato.fastdfs.service.FastFileStorageClient;
import com.wzkris.common.oss.config.OssConfig;
import com.wzkris.common.oss.domain.FileVO;
import com.wzkris.common.oss.service.FileService;
import org.apache.commons.io.FilenameUtils;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Service;

import java.io.InputStream;

/**
 * FastDFS 文件存储
 *
 * @author wzkris
 */
@RefreshScope
@Service
public class FastDfsServiceImpl implements FileService {

    private final OssConfig.FastDfsProperties properties;

    private final FastFileStorageClient storageClient;

    public FastDfsServiceImpl(OssConfig ossConfig, FastFileStorageClient storageClient) {
        this.properties = ossConfig.getFdfs();
        this.storageClient = storageClient;
    }

    @Override
    public String support() {
        return OssConfig.Fields.fdfs;
    }

    @Override
    public FileVO upload(InputStream is, String relativePath, String fileName, String contentType) {
        StorePath storePath = storageClient.uploadFile(is, -1, FilenameUtils.getExtension(fileName), null);
        return new FileVO(FilenameUtils.getName(fileName), properties.getDomain(), storePath.getFullPath());
    }

}
