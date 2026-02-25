package com.wzkris.common.httpclient.event;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

/**
 * http client调用事件
 *
 * @author wzkris
 */
@Data
@NoArgsConstructor
public class HttpClientCallEvent {

    private Integer httpStatusCode;

    private Map<String, String> requestHeaders;

    private String requestBody;

    private Map<String, String> responseHeaders;

    private String responseBody;

    private Long costTime;

}
