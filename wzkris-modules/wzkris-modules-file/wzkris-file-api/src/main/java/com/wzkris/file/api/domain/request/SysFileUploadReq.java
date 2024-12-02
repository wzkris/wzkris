package com.wzkris.file.api.domain.request;

import lombok.Data;

/**
 * 文件信息
 *
 * @author wzkris
 */
@Data
public class SysFileUploadReq {
    /**
     * 文件名称
     */
    private String name;

    /**
     * 文件地址
     */
    private String url;
}
