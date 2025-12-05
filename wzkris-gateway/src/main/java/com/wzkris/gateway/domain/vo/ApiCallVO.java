package com.wzkris.gateway.domain.vo;

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
public class ApiCallVO implements Serializable {

    /**
     * API接口调用总次数
     */
    private Long apiCallCount;

    /**
     * 调用成功次数
     */
    private Long successCount;

    /**
     * 调用失败次数
     */
    private Long errorCount;

}
