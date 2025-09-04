package com.wzkris.common.oss.service.impl;

import com.wzkris.common.oss.config.OssConfig;
import com.wzkris.common.oss.domain.FileVO;
import com.wzkris.common.oss.service.FileService;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import io.minio.errors.*;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

/**
 * Minio 文件存储
 *
 * @author wzkris
 */
@RefreshScope
@Service
public class MinioServiceImpl implements FileService {

    private final OssConfig.MinioProperties properties;

    private final MinioClient client;

    public MinioServiceImpl(OssConfig ossConfig) {
        this.properties = ossConfig.getMinio();
        this.client = MinioClient.builder()
                .endpoint(properties.getUrl())
                .credentials(properties.getAccessKey(), properties.getSecretKey())
                .build();
    }

    @Override
    public String support() {
        return OssConfig.Fields.minio;
    }

    @Override
    public FileVO upload(InputStream is, String relativePath, String fileName, String contentType) {
        try {
            PutObjectArgs args = PutObjectArgs.builder().bucket(relativePath).object(fileName).stream(
                            is, -1, 10 * 1024 * 1024)
                    .contentType(contentType)
                    .build();
            client.putObject(args);
            return new FileVO(fileName, properties.getUrl(), "/" + relativePath + "/" + fileName);
        } catch (ErrorResponseException
                 | InsufficientDataException
                 | InternalException
                 | InvalidKeyException
                 | InvalidResponseException
                 | IOException
                 | NoSuchAlgorithmException
                 | ServerException
                 | XmlParserException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

}
