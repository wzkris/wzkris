package com.wzkris.common.httpservice.event;

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
public class HttpRequestEvent {

    private String url;

    private String method;

    private String headers;

    private String params;

    private String body;

    private Long requestTime;

}
