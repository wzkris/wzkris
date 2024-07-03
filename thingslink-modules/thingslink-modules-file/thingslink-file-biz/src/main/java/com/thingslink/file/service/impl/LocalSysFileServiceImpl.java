package com.thingslink.file.service.impl;

import com.thingslink.common.core.exception.BusinessException;
import com.thingslink.common.core.utils.file.FileUtil;
import com.thingslink.file.config.LocalConfig;
import com.thingslink.file.domain.FileChunk;
import com.thingslink.file.service.SysFileService;
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
    private LocalConfig localConfig;

    @Override
    public String uploadFile(MultipartFile file) {
        String name = FileUtil.upload(localConfig.getPath(), file);
        return localConfig.getPrefix() + name;
    }

    @Override
    public void sliceUpload(FileChunk fileChunk) {
        // 拿到存储地址
        String absPath = FileUtil.uploadChunk(localConfig.getPath(), fileChunk.getChunk(), fileChunk.getMd5(), fileChunk.getMd5() + "_" + fileChunk.getOffset());
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
            }
            catch (IOException e) {
                throw new BusinessException(e.getMessage());
            }
            // 删除切片文件
            FileUtil.del(dir);
        }
    }
}
