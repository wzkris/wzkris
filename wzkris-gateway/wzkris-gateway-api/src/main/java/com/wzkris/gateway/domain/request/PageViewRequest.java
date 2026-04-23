package com.wzkris.gateway.domain.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 页面访问上报请求
 *
 * @author wzkris
 */
@Data
public class PageViewRequest {

    @Schema(description = "访问页面")
    private String view;

    @Schema(description = "是否成功")
    private Boolean success;

}

