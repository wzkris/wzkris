package com.wzkris.common.validator.impl;

import jakarta.validation.ConstraintValidatorContext;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.*;

/**
 * PhoneNumberValidator 测试类
 *
 * @author wzkris
 */
@DisplayName("手机号验证器测试")
class PhoneNumberValidatorTest {

    private final PhoneNumberValidator validator = new PhoneNumberValidator();
    private final ConstraintValidatorContext context = Mockito.mock(ConstraintValidatorContext.class);

    @Test
    @DisplayName("测试有效手机号 - 13开头")
    void testValidPhoneNumber_13() {
        String phone = "13812345678";
        assertTrue(PhoneNumberValidator.isPhoneNumber(phone));
        assertTrue(validator.isValid(phone, context));
    }

    @Test
    @DisplayName("测试有效手机号 - 14开头")
    void testValidPhoneNumber_14() {
        String phone = "14712345678";
        assertTrue(PhoneNumberValidator.isPhoneNumber(phone));
    }

    @Test
    @DisplayName("测试有效手机号 - 15开头")
    void testValidPhoneNumber_15() {
        String phone = "15012345678";
        assertTrue(PhoneNumberValidator.isPhoneNumber(phone));
    }

    @Test
    @DisplayName("测试有效手机号 - 16开头")
    void testValidPhoneNumber_16() {
        String phone = "16612345678";
        assertTrue(PhoneNumberValidator.isPhoneNumber(phone));
    }

    @Test
    @DisplayName("测试有效手机号 - 17开头")
    void testValidPhoneNumber_17() {
        String phone = "17812345678";
        assertTrue(PhoneNumberValidator.isPhoneNumber(phone));
    }

    @Test
    @DisplayName("测试有效手机号 - 18开头")
    void testValidPhoneNumber_18() {
        String phone = "18912345678";
        assertTrue(PhoneNumberValidator.isPhoneNumber(phone));
    }

    @Test
    @DisplayName("测试有效手机号 - 19开头")
    void testValidPhoneNumber_19() {
        String phone = "19912345678";
        assertTrue(PhoneNumberValidator.isPhoneNumber(phone));
    }

    @Test
    @DisplayName("测试无效手机号 - 12开头")
    void testInvalidPhoneNumber_12() {
        String phone = "12812345678";
        assertFalse(PhoneNumberValidator.isPhoneNumber(phone));
    }

    @Test
    @DisplayName("测试无效手机号 - 10开头")
    void testInvalidPhoneNumber_10() {
        String phone = "10812345678";
        assertFalse(PhoneNumberValidator.isPhoneNumber(phone));
    }

    @Test
    @DisplayName("测试无效手机号 - 20开头")
    void testInvalidPhoneNumber_20() {
        String phone = "20812345678";
        assertFalse(PhoneNumberValidator.isPhoneNumber(phone));
    }

    @Test
    @DisplayName("测试无效手机号 - 长度不足")
    void testInvalidPhoneNumber_TooShort() {
        assertFalse(PhoneNumberValidator.isPhoneNumber("1381234567")); // 10位
        assertFalse(PhoneNumberValidator.isPhoneNumber("138123456")); // 9位
    }

    @Test
    @DisplayName("测试无效手机号 - 长度过长")
    void testInvalidPhoneNumber_TooLong() {
        assertFalse(PhoneNumberValidator.isPhoneNumber("138123456789")); // 12位
        assertFalse(PhoneNumberValidator.isPhoneNumber("1381234567890")); // 13位
    }

    @Test
    @DisplayName("测试null值")
    void testNull() {
        assertTrue(validator.isValid(null, context));
        assertFalse(PhoneNumberValidator.isPhoneNumber(null));
    }

    @Test
    @DisplayName("测试空字符串")
    void testEmptyString() {
        assertFalse(PhoneNumberValidator.isPhoneNumber(""));
        assertFalse(PhoneNumberValidator.isPhoneNumber("   "));
    }

    @Test
    @DisplayName("测试包含空格")
    void testWithSpaces() {
        String phone = "138 1234 5678";
        // 验证器应该去除空格
        assertTrue(PhoneNumberValidator.isPhoneNumber(phone));
    }

    @Test
    @DisplayName("测试包含+86前缀")
    void testWithPlus86Prefix() {
        String phone = "+8613812345678";
        // 验证器应该去除+86前缀
        assertTrue(PhoneNumberValidator.isPhoneNumber(phone));
    }

    @Test
    @DisplayName("测试包含多个空格")
    void testWithMultipleSpaces() {
        String phone = "138  1234   5678";
        assertTrue(PhoneNumberValidator.isPhoneNumber(phone));
    }

    @Test
    @DisplayName("测试包含特殊字符")
    void testWithSpecialCharacters() {
        assertFalse(PhoneNumberValidator.isPhoneNumber("138-1234-5678"));
        assertFalse(PhoneNumberValidator.isPhoneNumber("138.1234.5678"));
        assertFalse(PhoneNumberValidator.isPhoneNumber("(138)12345678"));
    }

    @Test
    @DisplayName("测试包含字母")
    void testWithLetters() {
        assertFalse(PhoneNumberValidator.isPhoneNumber("1381234567a"));
        assertFalse(PhoneNumberValidator.isPhoneNumber("a3812345678"));
    }

    @Test
    @DisplayName("测试边界情况 - 130开头")
    void testBoundary_130() {
        String phone = "13012345678";
        assertTrue(PhoneNumberValidator.isPhoneNumber(phone));
    }

    @Test
    @DisplayName("测试边界情况 - 139开头")
    void testBoundary_139() {
        String phone = "13912345678";
        assertTrue(PhoneNumberValidator.isPhoneNumber(phone));
    }

    @Test
    @DisplayName("测试边界情况 - 199开头")
    void testBoundary_199() {
        String phone = "19912345678";
        assertTrue(PhoneNumberValidator.isPhoneNumber(phone));
    }

    @Test
    @DisplayName("测试所有有效号段")
    void testAllValidSegments() {
        int[] validPrefixes = {13, 14, 15, 16, 17, 18, 19};
        
        for (int prefix : validPrefixes) {
            for (int secondDigit = 0; secondDigit <= 9; secondDigit++) {
                String phone = prefix + "" + secondDigit + "12345678";
                assertTrue(PhoneNumberValidator.isPhoneNumber(phone), 
                    "Phone number " + phone + " should be valid");
            }
        }
    }

    @Test
    @DisplayName("测试完整格式 - 带+86和空格")
    void testCompleteFormat() {
        String phone = "+86 138 1234 5678";
        assertTrue(PhoneNumberValidator.isPhoneNumber(phone));
    }

    @Test
    @DisplayName("测试实际使用场景")
    void testRealWorldScenarios() {
        // 常见格式
        assertTrue(PhoneNumberValidator.isPhoneNumber("13812345678"));
        assertTrue(PhoneNumberValidator.isPhoneNumber(" 13812345678 "));
        assertTrue(PhoneNumberValidator.isPhoneNumber("+8613812345678"));
        assertTrue(PhoneNumberValidator.isPhoneNumber("+86 138 1234 5678"));
        
        // 无效格式
        assertFalse(PhoneNumberValidator.isPhoneNumber("02812345678")); // 固定电话格式
        assertFalse(PhoneNumberValidator.isPhoneNumber("4001234567")); // 400电话
        assertFalse(PhoneNumberValidator.isPhoneNumber("10086")); // 客服号码
    }
}

