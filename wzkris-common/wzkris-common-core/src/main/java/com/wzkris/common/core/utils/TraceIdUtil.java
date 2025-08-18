package com.wzkris.common.core.utils;

import com.wzkris.common.core.constant.HeaderConstants;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.slf4j.MDC;

import java.util.Date;
import java.util.Optional;

/**
 * traceId工具类
 *
 * @author wzkris
 */
public abstract class TraceIdUtil {

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
                .orElse(DateFormatUtils.format(new Date(), "yyyyMMddHHmmssSSS") + "-" +
                        RandomStringUtils.secure().next(4));
    }

}
