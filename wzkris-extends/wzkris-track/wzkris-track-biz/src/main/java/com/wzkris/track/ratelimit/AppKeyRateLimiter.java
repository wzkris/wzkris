package com.wzkris.track.ratelimit;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public final class AppKeyRateLimiter {

    private final int maxPerWindow;

    private final long windowMillis;

    private final ConcurrentHashMap<String, Window> windows = new ConcurrentHashMap<>();

    public AppKeyRateLimiter(int maxPerWindow, long windowMillis) {
        this.maxPerWindow = Math.max(1, maxPerWindow);
        this.windowMillis = Math.max(1L, windowMillis);
    }

    public boolean tryAcquire(String appKey) {
        long now = System.currentTimeMillis();
        Window w = windows.computeIfAbsent(appKey, k -> new Window());
        synchronized (w) {
            if (now - w.windowStartMillis >= windowMillis) {
                w.windowStartMillis = now;
                w.count.set(0);
            }
            int c = w.count.get();
            if (c >= maxPerWindow) {
                return false;
            }
            w.count.incrementAndGet();
            return true;
        }
    }

    private static final class Window {

        private final AtomicInteger count = new AtomicInteger();

        private long windowStartMillis = System.currentTimeMillis();

    }

}
