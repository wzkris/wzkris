package com.wzkris.common.remote.event;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

/**
 * 调用事件
 *
 * @author wzkris
 */
@Data
@NoArgsConstructor
public class RemoteCallEvent {

    private String httpMethod;

    private String httpUri;

    private Integer httpStatusCode;

    private Map<String, String> requestHeaders;

    private String requestBody;

    private Map<String, String> responseHeaders;

    private String responseBody;

    private Long costTime;

    private String errorMessage;

}
