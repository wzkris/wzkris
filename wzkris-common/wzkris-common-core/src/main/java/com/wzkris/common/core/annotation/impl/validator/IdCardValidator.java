package com.wzkris.common.core.annotation.impl.validator;

import com.wzkris.common.core.annotation.IdCard;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

/**
 * @author : wzkris
 * @version : V1.0.0
 * @description : 身份证校验器
 * @date : 2023/12/15 10:50
 */
public class IdCardValidator implements ConstraintValidator<IdCard, String> {

    // 身份证号码长度（15位或18位）
    private static final int LENGTH_15 = 15;

    private static final int LENGTH_18 = 18;

    // 18位身份证号码的校验码数组
    private static final char[] VERIFY_CODE = {'1', '0', 'X', '9', '8', '7', '6', '5', '4', '3', '2'};

    // 身份证号码中各位的权重因子
    private static final int[] WEIGHT_FACTOR = {7, 9, 10, 5, 8, 4, 2, 1, 6, 3, 7, 9, 10, 5, 8, 4, 2};

    /**
     * 验证身份证号码是否有效
     *
     * @param idCard 身份证号码
     * @return 是否有效
     */
    public static boolean isValidCard(String idCard) {
        if (idCard == null || idCard.isEmpty()) {
            return false;
        }

        // 去除空格并转为大写
        idCard = idCard.trim().toUpperCase();

        // 验证长度
        int length = idCard.length();
        if (length != LENGTH_15 && length != LENGTH_18) {
            return false;
        }

        // 15位身份证验证
        if (length == LENGTH_15) {
            return isValid15DigitIdCard(idCard);
        }

        // 18位身份证验证
        return isValid18DigitIdCard(idCard);
    }

    /**
     * 验证15位身份证号码
     */
    private static boolean isValid15DigitIdCard(String idCard) {
        // 验证全数字
        for (int i = 0; i < LENGTH_15; i++) {
            if (!Character.isDigit(idCard.charAt(i))) {
                return false;
            }
        }

        // 验证出生日期 (15位身份证号码的年份只有后两位，且默认是19xx年)
        int year = Integer.parseInt(idCard.substring(6, 8));
        int month = Integer.parseInt(idCard.substring(8, 10));
        int day = Integer.parseInt(idCard.substring(10, 12));

        return isValidDate(1900 + year, month, day);
    }

    /**
     * 验证18位身份证号码
     */
    private static boolean isValid18DigitIdCard(String idCard) {
        // 前17位必须是数字，最后一位可以是数字或X
        for (int i = 0; i < LENGTH_18 - 1; i++) {
            if (!Character.isDigit(idCard.charAt(i))) {
                return false;
            }
        }

        // 验证最后一位校验码
        char verifyChar = idCard.charAt(LENGTH_18 - 1);
        if (!Character.isDigit(verifyChar) && verifyChar != 'X') {
            return false;
        }

        // 验证出生日期
        int year = Integer.parseInt(idCard.substring(6, 10));
        int month = Integer.parseInt(idCard.substring(10, 12));
        int day = Integer.parseInt(idCard.substring(12, 14));

        if (!isValidDate(year, month, day)) {
            return false;
        }

        // 验证校验码
        return calculateVerifyCode(idCard) == verifyChar;
    }

    /**
     * 计算18位身份证的校验码
     */
    private static char calculateVerifyCode(String idCard) {
        int sum = 0;
        for (int i = 0; i < LENGTH_18 - 1; i++) {
            int digit = Character.getNumericValue(idCard.charAt(i));
            sum += digit * WEIGHT_FACTOR[i];
        }
        int mod = sum % 11;
        return VERIFY_CODE[mod];
    }

    /**
     * 验证日期是否有效
     */
    private static boolean isValidDate(int year, int month, int day) {
        // 简单验证年份范围
        if (year < 1900 || year > 2100) {
            return false;
        }

        // 验证月份范围
        if (month < 1 || month > 12) {
            return false;
        }

        // 验证每月天数
        int[] daysInMonth = {31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};

        // 闰年2月有29天
        if ((year % 4 == 0 && year % 100 != 0) || year % 400 == 0) {
            daysInMonth[1] = 29;
        }

        return day >= 1 && day <= daysInMonth[month - 1];
    }

    @Override
    public boolean isValid(String idCard, ConstraintValidatorContext constraintValidatorContext) {
        if (idCard == null) {
            return true;
        }
        return isValidCard(idCard);
    }

}
