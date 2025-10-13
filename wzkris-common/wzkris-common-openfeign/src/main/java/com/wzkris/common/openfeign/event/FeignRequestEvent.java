package com.wzkris.common.openfeign.event;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * feign请求事件
 *
 * @author wzkris
 * @date 2025/10/13
 */
@Data
@NoArgsConstructor
public class FeignRequestEvent {

    private String url;

    private String method;

    private String headers;

    private String params;

    private String body;

    private Long requestTime;

}
