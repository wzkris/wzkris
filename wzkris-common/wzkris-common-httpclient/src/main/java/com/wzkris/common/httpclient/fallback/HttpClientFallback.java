package com.wzkris.common.httpclient.fallback;

/**
 * Factory used to build fallback instances when a HTTP service call fails.
 *
 * @param <T> service interface type
 */
public interface HttpClientFallback<T> {

    /**
     * Creates a fallback instance for the given cause.
     */
    T create(Throwable cause);

    /**
     * Marker fallback used when no fallback should be applied.
     */
    final class NoOp implements HttpClientFallback<Object> {

        @Override
        public Object create(Throwable cause) {
            throw new UnsupportedOperationException("No HttpService fallback configured");
        }

    }

}
