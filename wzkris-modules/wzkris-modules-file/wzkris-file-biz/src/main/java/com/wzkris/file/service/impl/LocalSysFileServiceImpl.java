package com.wzkris.file.service.impl;

import com.wzkris.common.core.exception.service.GenericException;
import com.wzkris.file.config.LocalProperties;
import com.wzkris.file.domain.FileChunk;
import com.wzkris.file.service.SysFileService;
import com.wzkris.file.utils.FileUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.IOException;

/**
 * 本地文件存储
 *
 * @author wzkris
 */
@Primary
@Service
public class LocalSysFileServiceImpl implements SysFileService {

    @Autowired
    private LocalProperties localProperties;

    @Override
    public String uploadFile(MultipartFile file) {
        String name = FileUtil.upload(localProperties.getPath(), file);
        return localProperties.getPrefix() + name;
    }

    @Override
    public void sliceUpload(FileChunk fileChunk) {
        // 拿到存储地址
        String absPath = FileUtil.uploadChunk(localProperties.getPath(), fileChunk.getChunk(), fileChunk.getMd5(), fileChunk.getMd5() + "_" + fileChunk.getOffset());
        // 相等则全部上传完毕，开始逐步合并
        if (fileChunk.getOffset() == fileChunk.getTotal()) {
            // 获取存储路径、父路径
            String dir = FileUtil.getParent(absPath, 1);
            String parentDir = FileUtil.getParent(absPath, 2);

            try {
                BufferedOutputStream ost = FileUtil.getOutputStream(parentDir + File.separator + fileChunk.getFileName());
                for (int i = 1; i <= fileChunk.getTotal(); i++) {
                    String filePath = dir + File.separator + fileChunk.getMd5() + "_" + i;
                    byte[] bytes = FileUtil.readBytes(filePath);
                    ost.write(bytes);
                }
            } catch (IOException e) {
                throw new GenericException(e.getMessage());
            }
            // 删除切片文件
            FileUtil.del(dir);
        }
    }
}
