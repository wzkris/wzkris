package com.wzkris.common.openfeign.event;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * feign响应事件
 *
 * @author wzkris
 * @date 2025/10/13
 */
@Data
@NoArgsConstructor
public class FeignResponseEvent {

    private boolean success;

    private Integer httpStatusCode;

    private String body;

    private Integer costTime;

}
