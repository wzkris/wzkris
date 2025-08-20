package com.wzkris.common.core.threads;

import com.wzkris.common.core.utils.TraceIdUtil;

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
        this(delegate, TraceIdUtil.get());
    }

    @Override
    public void run() {
        TraceIdUtil.set(traceId);
        try {
            delegate.run();
        } finally {
            TraceIdUtil.clear();
        }
    }

}
