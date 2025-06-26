package com.wzkris.common.core.annotation.impl.validator;

import com.wzkris.common.core.annotation.PhoneNumber;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

/**
 * @author : wzkris
 * @version : V1.0.0
 * @description : 手机号校验器
 * @date : 2023/12/14 11:18
 */
public class PhoneNumberValidator implements ConstraintValidator<PhoneNumber, String> {

    // 中国手机号码正则表达式
    private static final String PHONE_REGEX = "^1[3-9]\\d{9}$";

    /**
     * 验证手机号是否有效
     *
     * @param phoneNumber 手机号
     * @return 是否有效
     */
    public static boolean isPhoneNumber(String phoneNumber) {
        if (phoneNumber == null || phoneNumber.isEmpty()) {
            return false;
        }

        // 去除空格和特殊字符
        phoneNumber = phoneNumber.replaceAll("\\s+", "");
        phoneNumber = phoneNumber.replaceAll("^\\+86", ""); // 去除可能的+86前缀

        // 使用正则表达式验证
        return phoneNumber.matches(PHONE_REGEX);
    }

    @Override
    public boolean isValid(String phoneNumber, ConstraintValidatorContext constraintValidatorContext) {
        if (phoneNumber == null) {
            return true;
        }
        return isPhoneNumber(phoneNumber);
    }

}
