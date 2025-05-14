package com.wzkris.common.oss.service.impl;

import cn.hutool.core.date.DateUtil;
import com.wzkris.common.core.utils.StringUtil;
import com.wzkris.common.oss.config.OssConfig;
import com.wzkris.common.oss.domain.FileVO;
import com.wzkris.common.oss.service.FileService;
import com.wzkris.common.oss.utils.FileUtil;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;

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
            String pathName = relativePath + formatPathName(fileName);
            FileUtil.writeBytes(is.readAllBytes(), properties.getPath() + "/" + pathName);
            return new FileVO(FileUtil.getName(pathName), properties.getPrefix(), pathName);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 转换文件名
     */
    public String formatPathName(String pathName) {
        // 以日期作为子目录
        return DateUtil.format(DateUtil.date(), "/yyyy/MM/dd/")
                + StringUtil.format("{}_{}.{}", FileUtil.getPrefix(pathName), System.currentTimeMillis(), FileUtil.getSuffix(pathName));
    }

}
