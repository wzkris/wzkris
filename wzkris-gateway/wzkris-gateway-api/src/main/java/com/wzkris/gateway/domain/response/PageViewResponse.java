package com.wzkris.gateway.domain.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;

/**
 * 页面访问统计响应（页面PV、UV等数据）
 *
 * @author wzkris
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PageViewResponse implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Schema(description = "页面浏览量（PV）")
    private Integer pv;

    @Schema(description = "独立访客数（UV）")
    private Integer uv;

}

