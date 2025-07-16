package com.wzkris.common.orm.utils;

import com.wzkris.common.orm.annotation.DataScope;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author : wzkris
 * @version : V1.0.0
 * @description : 数据权限工具
 * @date : 2023/12/11 10:54
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class DataScopeUtil {

    private static final Map<String, DataScope> methodCache = new ConcurrentHashMap<>();

    private static final ThreadLocal<Map<String, Object>> CONTEXT = ThreadLocal.withInitial(HashMap::new);

    public static DataScope getDataScope(String methodName) {
        return methodCache.get(methodName);
    }

    public static void setDataScope(String methodName, DataScope dataScope) {
        methodCache.put(methodName, dataScope);
    }

    public static Object getParameter(String column) {
        return CONTEXT.get() == null ? null : CONTEXT.get().get(column);
    }

    public static void putParameter(String column, Object value) {
        Map<String, Object> parameter = CONTEXT.get();
        if (parameter == null) {
            parameter = new HashMap<>();
        }
        parameter.put(column, value);
        CONTEXT.set(parameter);
    }

    public static void remove() {
        CONTEXT.remove();
    }

}
