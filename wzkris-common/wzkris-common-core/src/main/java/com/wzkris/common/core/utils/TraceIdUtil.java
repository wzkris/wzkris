package com.wzkris.common.core.utils;

import com.wzkris.common.core.constant.HeaderConstants;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.FastDateFormat;
import org.slf4j.MDC;

import java.util.Date;
import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicLong;

/**
 * traceId工具类
 *
 * @author wzkris
 */
public abstract class TraceIdUtil {

    static final FastDateFormat df = FastDateFormat.getInstance("yyyyMMddHHmmssSSS", null, null);

    static final AtomicLong SEQUENCE = new AtomicLong(0);

    public static void set(String id) {
        MDC.put(HeaderConstants.X_TRACING_ID, StringUtils.isNoneEmpty(id) ? id : get());
    }

    public static void set() {
        MDC.put(HeaderConstants.X_TRACING_ID, get());
    }

    public static void clear() {
        MDC.clear();
    }

    public static String get() {
        return Optional.ofNullable(MDC.get(HeaderConstants.X_TRACING_ID))
                .orElse(df.format(new Date()) + "-" + SEQUENCE.getAndIncrement() +
                        "-" + ThreadLocalRandom.current().nextInt(9_999_999));
    }

}
