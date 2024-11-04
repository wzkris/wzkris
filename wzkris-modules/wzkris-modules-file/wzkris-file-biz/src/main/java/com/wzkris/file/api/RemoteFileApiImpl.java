package com.wzkris.file.api;

import com.wzkris.common.core.domain.Result;
import com.wzkris.common.openfeign.annotation.InnerAuth;
import com.wzkris.file.api.domain.SysFile;
import com.wzkris.file.service.SysFileService;
import io.swagger.v3.oas.annotations.Hidden;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import static com.wzkris.common.core.domain.Result.success;

/**
 * @author : wzkris
 * @version : V1.0.0
 * @description : rpc - 文件rpc调用
 * @date : 2023/3/13 16:26
 */
@Hidden
@InnerAuth
@RestController
@RequiredArgsConstructor
public class RemoteFileApiImpl implements RemoteFileApi {

    private final SysFileService fileService;

    // 上传文件
    public Result<SysFile> upload(MultipartFile file) {
        // 上传并返回访问地址
        String url = fileService.uploadFile(file);
        SysFile sysFile = new SysFile();
        sysFile.setName(sysFile.getName());
        sysFile.setUrl(url);
        return success(sysFile);
    }
}
