package com.wzkris.common.core.utils;

import com.wzkris.common.core.constant.CustomHeaderConstants;
import org.apache.commons.lang3.time.FastDateFormat;
import org.slf4j.MDC;
import org.springframework.lang.Nullable;

import java.util.Date;
import java.util.TimeZone;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicLong;

/**
 * traceId工具类
 *
 * @author wzkris
 */
public abstract class TraceIdUtil {

    static final FastDateFormat df = FastDateFormat.getInstance("yyyyMMddHHmmssSSS", TimeZone.getTimeZone("GMT+8"), null);

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
        return df.format(new Date()) + "-" + SEQUENCE.getAndIncrement() +
                "-" + ThreadLocalRandom.current().nextInt(9_999_999);
    }

}
