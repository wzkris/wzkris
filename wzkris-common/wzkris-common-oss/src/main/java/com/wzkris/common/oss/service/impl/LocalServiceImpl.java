package com.wzkris.common.oss.service.impl;

import com.wzkris.common.core.utils.StringUtil;
import com.wzkris.common.oss.config.OssConfig;
import com.wzkris.common.oss.domain.FileVO;
import com.wzkris.common.oss.service.FileService;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Date;

/**
 * 本地文件存储
 *
 * @author wzkris
 */
@RefreshScope
@Service
public class LocalServiceImpl implements FileService {

    private final OssConfig.LocalProperties properties;

    public LocalServiceImpl(OssConfig ossConfig) {
        this.properties = ossConfig.getLocal();
    }

    @Override
    public String support() {
        return OssConfig.Fields.local;
    }

    @Override
    public FileVO upload(InputStream is, String relativePath, String fileName, String contentType) {
        try {
            // 使用Lang3的StringUtils处理路径
            String safeFileName = StringUtil.replace(fileName, ".", "_");
            String pathName = relativePath + safeFileName;

            // 使用Java NIO替换FileUtil
            Path targetPath = Paths.get(properties.getPath(), pathName);
            Files.createDirectories(targetPath.getParent());

            // 使用Files.copy替代FileUtil.writeBytes
            Files.copy(is, targetPath, StandardCopyOption.REPLACE_EXISTING);

            return new FileVO(
                    Paths.get(pathName).getFileName().toString(),
                    properties.getPrefix(),
                    pathName
            );
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 转换文件名
     */
    public String formatPathName(String pathName) {
        // 以日期作为子目录
        try {
            return DateFormatUtils.format(new Date(), "/yyyy/MM/dd/")
                    + String.format(
                    "%s_%s.%s",
                    FilenameUtils.getPrefix(pathName),
                    System.currentTimeMillis(),
                    FilenameUtils.getExtension(pathName));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
