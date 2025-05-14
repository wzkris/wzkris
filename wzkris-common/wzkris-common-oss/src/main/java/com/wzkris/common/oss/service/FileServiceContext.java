package com.wzkris.common.oss.service;

import com.wzkris.common.oss.config.OssConfig;
import lombok.RequiredArgsConstructor;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FileServiceContext {

    private final List<FileService> fileServices;

    private final OssConfig ossConfig;

    @RefreshScope
    public FileService getContext() {
        return fileServices.stream()
                .filter(s -> s.support().equals(ossConfig.getUse()))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("No file service found !!!"));
    }
}
