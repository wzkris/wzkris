package com.wzkris.file.controller;

import com.wzkris.common.core.domain.Result;
import com.wzkris.common.security.oauth2.annotation.CheckSystemPerms;
import com.wzkris.common.web.model.BaseController;
import com.wzkris.file.api.domain.request.SysFileUploadReq;
import com.wzkris.file.domain.FileChunk;
import com.wzkris.file.service.SysFileService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 * 文件请求处理
 *
 * @author wzkris
 */
@Tag(name = "文件处理")
@RestController
@RequiredArgsConstructor
public class SysFileController extends BaseController {

    private final SysFileService fileService;

    @Operation(summary = "小文件上传")
    @PostMapping("/upload")
    @CheckSystemPerms("normal:upload")
    public Result<SysFileUploadReq> upload(@Parameter(description = "上传文件(大小限制在15Mb)")
                                           @RequestParam MultipartFile file) {
        // 上传并返回访问地址
        String url = fileService.uploadFile(file);
        SysFileUploadReq sysFileUploadReq = new SysFileUploadReq();
        sysFileUploadReq.setName(sysFileUploadReq.getName());
        sysFileUploadReq.setUrl(url);
        return ok(sysFileUploadReq);
    }

    @Operation(summary = "切片文件上传")
    @PostMapping("/slice/upload")
    @CheckSystemPerms("slice:upload")
    public Result<?> sliceUpload(FileChunk fileChunk) {
        // TODO 查询文件是否存在，存在则秒传
        fileService.sliceUpload(fileChunk);
        return ok();
    }
}
