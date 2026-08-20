package com.wzkris.common.remote.interceptor;

import org.springframework.http.client.ClientHttpRequestInterceptor;

/**
 * Marker interface for interceptors that should only apply to remote interface clients.
 */
public interface RemoteClientRequestInterceptor extends ClientHttpRequestInterceptor {

}
