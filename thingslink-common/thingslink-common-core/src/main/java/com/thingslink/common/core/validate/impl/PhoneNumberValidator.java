package com.thingslink.common.core.validate.impl;

import cn.hutool.core.util.PhoneUtil;
import com.thingslink.common.core.validate.PhoneNumber;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

/**
 * @author : wzkris
 * @version : V1.0.0
 * @description : 手机号校验器
 * @date : 2023/12/14 11:18
 */
public class PhoneNumberValidator implements ConstraintValidator<PhoneNumber, String> {

    @Override
    public boolean isValid(String phoneNumber, ConstraintValidatorContext constraintValidatorContext) {
        if (phoneNumber == null) {
            return true;
        }
        return PhoneUtil.isPhone(phoneNumber);
    }
}
