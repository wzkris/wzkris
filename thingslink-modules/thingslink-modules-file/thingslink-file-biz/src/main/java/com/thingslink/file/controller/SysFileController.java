package com.thingslink.file.controller;

import com.thingslink.common.core.domain.Result;
import com.thingslink.common.web.controller.BaseController;
import com.thingslink.file.api.domain.SysFile;
import com.thingslink.file.domain.FileChunk;
import com.thingslink.file.service.SysFileService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
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
    @PreAuthorize("hasAuthority('normal:upload')")
    public Result<SysFile> upload(@Parameter(description = "上传文件(大小限制在15Mb)")
                                  @RequestParam MultipartFile file) {
        // 上传并返回访问地址
        String url = fileService.uploadFile(file);
        SysFile sysFile = new SysFile();
        sysFile.setName(sysFile.getName());
        sysFile.setUrl(url);
        return success(sysFile);
    }

    @Operation(summary = "切片文件上传")
    @PostMapping("/slice/upload")
    @PreAuthorize("hasAuthority('slice:upload')")
    public Result<?> sliceUpload(FileChunk fileChunk) {
        // TODO 查询文件是否存在，存在则秒传
        fileService.sliceUpload(fileChunk);
        return success();
    }
}
