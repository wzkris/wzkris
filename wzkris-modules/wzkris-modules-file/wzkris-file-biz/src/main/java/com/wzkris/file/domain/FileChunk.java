package com.wzkris.file.domain;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author : wzkris
 * @version : V1.0.0
 * @description : 文件块
 * @date : 2023/9/15 11:00
 */
@Data
public class FileChunk {
    @Schema(description = "文件名")
    private String fileName;

    @Schema(description = "文件摘要")
    private String md5;

    @Schema(description = "总数量")
    private int total;

    @Schema(description = "偏移量(从1开始)")
    private int offset;

    @Schema(description = "文件块")
    private MultipartFile chunk;
}
