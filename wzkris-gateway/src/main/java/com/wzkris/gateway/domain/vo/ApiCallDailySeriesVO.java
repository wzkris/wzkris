package com.wzkris.gateway.domain.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Map;

/**
 * API调用（日级）返回，内含24小时序列
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ApiCallDailySeriesVO implements Serializable {

    /**
     * 日期（yyyy-MM-dd）
     */
    private String date;

    /**
     * 当日总计
     */
    private ApiCallVO total;

    /**
     * 小时序列：key=yyyy-MM-dd-HH，value=该小时的api调用/成功/失败
     */
    private Map<String, ApiCallVO> hours;

    /**
     * 按路径的当日总计：key=path，value=该path的api总数/成功/失败
     */
    private Map<String, ApiCallVO> paths;

}


