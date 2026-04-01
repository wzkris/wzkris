package com.wzkris.gateway.domain.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Map;

/**
 * 页面访问（日级）返回，内含24小时序列
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PageViewDailySeriesResponse implements Serializable {

    /**
     * 日期（yyyy-MM-dd）
     */
    private String date;

    /**
     * 当日总计
     */
    private PageViewResponse total;

    /**
     * 小时序列：key=yyyy-MM-dd-HH，value=该小时的pv/uv
     * 使用LinkedHashMap保证0-23顺序
     */
    private Map<String, PageViewResponse> hours;

}

