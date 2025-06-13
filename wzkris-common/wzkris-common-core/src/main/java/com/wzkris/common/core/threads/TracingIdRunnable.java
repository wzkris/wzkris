package com.wzkris.common.core.threads;

import com.wzkris.common.core.constant.HeaderConstants;
import org.slf4j.MDC;

/**
 * tracing_id包装任务
 *
 * @author wzkris
 */
public class TracingIdRunnable implements Runnable {

    private final Runnable delegate;

    private final String traceId;

    public TracingIdRunnable(Runnable delegate, String traceId) {
        this.delegate = delegate;
        this.traceId = traceId;
    }

    public TracingIdRunnable(Runnable delegate) {
        this(delegate, MDC.get(HeaderConstants.X_TRACING_ID));
    }

    @Override
    public void run() {
        MDC.put(HeaderConstants.X_TRACING_ID, traceId);
        try {
            delegate.run();
        } finally {
            MDC.clear();
        }
    }

}
