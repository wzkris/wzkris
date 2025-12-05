package com.wzkris.gateway.domain.req;

import lombok.Data;

/**
 * PV 上报请求
 */
@Data
public class PageViewReq {

    /**
     * 访问页面
     */
    private String view;

    /**
     * 是否成功
     */
    private Boolean success;

}
