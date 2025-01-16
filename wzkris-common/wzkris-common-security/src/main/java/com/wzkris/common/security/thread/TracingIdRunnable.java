package com.wzkris.common.security.thread;

import com.wzkris.common.core.constant.CommonConstants;
import org.slf4j.MDC;

/**
 * tracing_id包装任务
 */
public class TracingIdRunnable implements Runnable {

    private final Runnable delegate;

    private final String traceId;

    public TracingIdRunnable(Runnable delegate, String traceId) {
        this.delegate = delegate;
        this.traceId = traceId;
    }

    @Override
    public void run() {
        MDC.put(CommonConstants.TRACING_ID, traceId);
        try {
            delegate.run();
        } finally {
            MDC.clear();
        }
    }
}
