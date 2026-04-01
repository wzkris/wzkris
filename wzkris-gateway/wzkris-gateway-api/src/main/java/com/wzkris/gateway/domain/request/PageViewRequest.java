package com.wzkris.gateway.domain.request;

import lombok.Data;

/**
 * PV 上报请求
 */
@Data
public class PageViewRequest {

    /**
     * 访问页面
     */
    private String view;

    /**
     * 是否成功
     */
    private Boolean success;

}

