package com.wzkris.common.core.utils;

import com.wzkris.common.core.constant.CustomHeaderConstants;
import org.slf4j.MDC;
import org.springframework.lang.Nullable;

import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicLong;

/**
 * traceId工具类
 *
 * @author wzkris
 */
public abstract class TraceIdUtil {

    static final DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyyMMddHHmmssSSS").withZone(ZoneId.of("GMT+8"));

    static final AtomicLong SEQUENCE = new AtomicLong(0);

    public static void set(String traceId) {
        if (StringUtil.isNotBlank(traceId)) {
            MDC.put(CustomHeaderConstants.X_TRACING_ID, traceId);
        }
    }

    public static void setHint(@Nullable String hint) {
        if (StringUtil.isNotBlank(hint)) {
            MDC.put(CustomHeaderConstants.X_ROUTE_HINT, hint);
        }
    }

    public static void clear() {
        MDC.clear();
    }

    public static String get() {
        return MDC.get(CustomHeaderConstants.X_TRACING_ID);
    }

    public static String getOrGenerate() {
        String traceId = MDC.get(CustomHeaderConstants.X_TRACING_ID);
        return StringUtil.isNotBlank(traceId) ? traceId : generate();
    }

    public static String generate() {
        return df.format(Instant.now()) + "-" + SEQUENCE.getAndIncrement() +
                "-" + ThreadLocalRandom.current().nextInt(9_999_999);
    }

}
