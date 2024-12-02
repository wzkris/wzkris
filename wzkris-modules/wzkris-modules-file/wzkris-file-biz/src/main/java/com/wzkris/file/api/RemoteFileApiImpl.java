package com.wzkris.file.api;

import com.wzkris.common.core.domain.Result;
import com.wzkris.common.openfeign.annotation.InnerAuth;
import com.wzkris.file.api.domain.request.SysFileUploadReq;
import com.wzkris.file.service.SysFileService;
import io.swagger.v3.oas.annotations.Hidden;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import static com.wzkris.common.core.domain.Result.ok;

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
    public Result<SysFileUploadReq> upload(MultipartFile file) {
        // 上传并返回访问地址
        String url = fileService.uploadFile(file);
        SysFileUploadReq sysFileUploadReq = new SysFileUploadReq();
        sysFileUploadReq.setName(sysFileUploadReq.getName());
        sysFileUploadReq.setUrl(url);
        return ok(sysFileUploadReq);
    }
}
