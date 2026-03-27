package com.wzkris.gateway.domain.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * API调用量统计VO
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ApiCallResponse implements Serializable {

    /**
     * API接口调用总次数
     */
    private Integer apiCallCount;

    /**
     * 调用成功次数
     */
    private Integer successCount;

    /**
     * 调用失败次数
     */
    private Integer errorCount;

}

