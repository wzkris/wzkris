package com.wzkris.common.core.annotation.impl.validator;

import com.wzkris.common.core.annotation.EnumsCheck;
import com.wzkris.common.core.utils.StringUtil;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

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
        for (String str : enumsCheck.values()) {
            if (StringUtil.equals(value, str)) {
                return true;
            }
        }
        return false;
    }

}
