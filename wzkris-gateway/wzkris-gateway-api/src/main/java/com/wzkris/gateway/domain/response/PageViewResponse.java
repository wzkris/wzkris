package com.wzkris.gateway.domain.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 页面访问统计VO（页面PV、UV等数据，不混用API调用量）
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PageViewResponse implements Serializable {

    /**
     * 页面浏览量（PV）
     */
    private Integer pv;

    /**
     * 独立访客数（UV）
     */
    private Integer uv;

}

