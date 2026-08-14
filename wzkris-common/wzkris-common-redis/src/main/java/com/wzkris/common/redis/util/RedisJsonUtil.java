package com.wzkris.common.redis.util;

import com.fasterxml.jackson.core.type.TypeReference;
import com.wzkris.common.core.utils.JsonUtil;
import org.springframework.lang.Nullable;

/**
 * Redis 值读取工具。
 * <p>
 * 值序列化器不携带类型信息（clean JSON，无 @class），读取结果是
 * LinkedHashMap / ArrayList / String 等自然类型，统一在此转换为目标类型。
 *
 * @author wzkris
 */
public final class RedisJsonUtil {

    private RedisJsonUtil() {
    }

    /**
     * 将 Redis 读取到的值转换为目标类型。
     * <ul>
     *   <li>值为 null 时返回 null</li>
     *   <li>值已是目标类型时直接返回</li>
     *   <li>值为 JSON 字符串时按字符串解析</li>
     *   <li>其余（Map / List 等自然类型）由 ObjectMapper 转换</li>
     * </ul>
     *
     * @param value Redis 读取到的值（可能是 null、目标类型实例、JSON 字符串或自然类型）
     * @param type  目标类型
     * @param <T>   目标类型
     * @return 转换后的目标类型实例，值为 null 时返回 null
     */
    @Nullable
    public static <T> T parse(@Nullable Object value, Class<T> type) {
        if (value == null) {
            return null;
        }
        if (type.isInstance(value)) {
            return type.cast(value);
        }
        if (value instanceof String json) {
            if (json.isEmpty()) {
                return null;
            }
            return JsonUtil.parseObject(json, type);
        }
        return JsonUtil.convertValue(value, type);
    }

    /**
     * 支持泛型目标类型（如 {@code List<X>}、{@code Map<K,V>}）的读取转换。
     * <p>
     * 泛型集合因类型擦除无法用 {@link Class} 表达元素类型，需用 {@link TypeReference}：
     * <pre>{@code
     * List<MenuResponse> list = RedisJsonUtil.parse(value, new TypeReference<List<MenuResponse>>() {});
     * }</pre>
     */
    @Nullable
    public static <T> T parse(@Nullable Object value, TypeReference<T> type) {
        if (value == null) {
            return null;
        }
        if (value instanceof String json) {
            if (json.isEmpty()) {
                return null;
            }
            return JsonUtil.parseObject(json, type);
        }
        return JsonUtil.convertValue(value, type);
    }

}
