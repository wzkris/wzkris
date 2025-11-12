package com.wzkris.common.validator.impl;

import com.wzkris.common.validator.annotation.EnumsCheck;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.apache.commons.lang3.reflect.FieldUtils;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author : wzkris
 * @version : V1.0.0
 * @description : 枚举固定值校验器
 * @date : 2024/12/25 16:40
 */
public class EnumsCheckValidator implements ConstraintValidator<EnumsCheck, String> {

    private EnumsCheck enumsCheck;

    @Override
    public void initialize(EnumsCheck constraintAnnotation) {
        this.enumsCheck = constraintAnnotation;
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null) {
            return true;
        }

        Set<String> validValues = Arrays.stream(enumsCheck.value().getEnumConstants())
                .map(enumConstant -> {
                    if (enumsCheck.property().isEmpty()) {
                        // 如果没有指定属性，校验枚举名称
                        return enumConstant.name();
                    } else {
                        // 校验指定属性的值
                        Field field = FieldUtils.getField(enumsCheck.value(), enumsCheck.property(), true);
                        try {
                            Object fieldValue = field.get(enumConstant);
                            return fieldValue != null ? fieldValue.toString() : null;
                        } catch (Exception e) {
                            throw new IllegalArgumentException("无法访问枚举属性: " + enumsCheck.property(), e);
                        }
                    }
                })
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());

        return validValues.contains(value);
    }

}
