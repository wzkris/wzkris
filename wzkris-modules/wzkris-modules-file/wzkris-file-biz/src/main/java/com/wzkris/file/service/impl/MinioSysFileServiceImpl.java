package com.wzkris.file.service.impl;

import com.wzkris.common.core.utils.file.FileUtil;
import com.wzkris.file.config.MinioConfig;
import com.wzkris.file.domain.FileChunk;
import com.wzkris.file.service.SysFileService;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import io.minio.errors.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

/**
 * Minio 文件存储
 *
 * @author wzkris
 */
@Service
public class MinioSysFileServiceImpl implements SysFileService {
    @Autowired
    private MinioConfig minioConfig;

    @Autowired
    private MinioClient client;

    /**
     * 本地文件上传接口
     *
     * @param file 上传的文件
     * @return 访问地址
     */
    @Override
    public String uploadFile(MultipartFile file) {
        String fileName = FileUtil.extractFilename(file);
        try {
            PutObjectArgs args = PutObjectArgs.builder()
                    .bucket(minioConfig.getBucketName())
                    .object(fileName)
                    .stream(file.getInputStream(), file.getSize(), -1)
                    .contentType(file.getContentType())
                    .build();
            client.putObject(args);
        }
        catch (ErrorResponseException | InsufficientDataException | InternalException | InvalidKeyException |
               InvalidResponseException | IOException | NoSuchAlgorithmException | ServerException |
               XmlParserException e) {
            throw new RuntimeException(e.getMessage());
        }
        return minioConfig.getBucketName() + "/" + fileName;
    }

    @Override
    public void sliceUpload(FileChunk fileChunk) {
    }
}
