package com.wzkris.common.web.config;

import org.springframework.core.convert.converter.Converter;
import org.springframework.core.convert.converter.ConverterFactory;
import org.springframework.format.FormatterRegistry;
import org.springframework.lang.NonNull;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 查询参数等场景下按枚举 {@code value}（与 {@code @JsonValue} 一致）绑定，而非按常量名。
 */
public class EnumConvertWebMvcConfigurer implements WebMvcConfigurer {

    @Override
    public void addFormatters(@NonNull FormatterRegistry registry) {
        registry.addConverterFactory(new StringToEnumByValueConverterFactory());
    }

    private static final class StringToEnumByValueConverterFactory implements ConverterFactory<String, Enum<?>> {

        private final Map<Class<?>, Method> fromValueMethodCache = new ConcurrentHashMap<>();

        @Override
        public <T extends Enum<?>> Converter<String, T> getConverter(Class<T> targetType) {
            Method fromValueMethod = fromValueMethodCache.computeIfAbsent(targetType, this::resolveFromValueMethod);
            return source -> convert(source, targetType, fromValueMethod);
        }

        private Method resolveFromValueMethod(Class<?> targetType) {
            try {
                return targetType.getMethod("fromValue", String.class);
            } catch (NoSuchMethodException ignored) {
                return null;
            }
        }

        private <T extends Enum<?>> T convert(String source, Class<T> targetType, Method fromValueMethod) {
            if (source == null || source.isBlank()) {
                return null;
            }
            if (fromValueMethod != null) {
                try {
                    T enumValue = targetType.cast(fromValueMethod.invoke(null, source));
                    if (enumValue != null) {
                        return enumValue;
                    }
                } catch (IllegalAccessException | InvocationTargetException e) {
                    throw new IllegalArgumentException("Failed to convert value: " + source, e);
                }
            }
            String enumName = source.trim().toUpperCase(Locale.ROOT);
            for (T enumConstant : targetType.getEnumConstants()) {
                if (enumConstant.name().equals(enumName)) {
                    return enumConstant;
                }
            }
            throw new IllegalArgumentException("No enum constants " + targetType.getName() + "." + enumName);
        }

    }

}
