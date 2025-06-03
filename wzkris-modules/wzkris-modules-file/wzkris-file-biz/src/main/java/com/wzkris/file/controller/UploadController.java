package com.wzkris.file.controller;

import com.wzkris.common.core.domain.Result;
import com.wzkris.common.oss.domain.FileVO;
import com.wzkris.common.oss.service.FileServiceContext;
import com.wzkris.common.web.model.BaseController;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.io.IOException;
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
@Tag(name = "文件上传")
@RestController
@RequiredArgsConstructor
public class UploadController extends BaseController {

    private final FileServiceContext fileServiceContext;

    @Operation(summary = "小文件上传")
    @PostMapping("/upload")
    public Result<FileVO> upload(@RequestParam MultipartFile file, @RequestParam String path) throws IOException {
        return ok(fileServiceContext
                .getContext()
                .upload(file.getInputStream(), path, file.getOriginalFilename(), file.getContentType()));
    }

    //    @Operation(summary = "切片文件上传")
    //    @PostMapping("/slice/upload")
    //    public Result<?> sliceUpload(FileChunk fileChunk) {
    //        // TODO 查询文件是否存在，存在则秒传
    //        fileServiceContext.getContext().sliceUpload(fileChunk);
    //        return ok();
    //    }
}
