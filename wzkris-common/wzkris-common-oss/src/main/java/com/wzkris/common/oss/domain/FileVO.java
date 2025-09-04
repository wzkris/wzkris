package com.wzkris.common.oss.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 返回文件体
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class FileVO {

    /**
     * 文件名
     */
    private String name;

    /**
     * 前缀路径
     */
    private String prefixPath;

    /**
     * url
     */
    private String url;

}
