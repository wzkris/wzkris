package com.wzkris.common.httpservice.event;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

/**
 * http service client调用事件
 *
 * @author wzkris
 */
@Data
@NoArgsConstructor
public class HttpServiceCallEvent {

    private Integer httpStatusCode;

    private Map<String, String> requestHeaders;

    private String requestBody;

    private Map<String, String> responseHeaders;

    private String responseBody;

    private Long costTime;

}
