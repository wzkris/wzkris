package com.thingslink.common.core.validate.impl;

import cn.hutool.core.util.IdcardUtil;
import com.thingslink.common.core.validate.IdCard;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

/**
 * @author : wzkris
 * @version : V1.0.0
 * @description : 身份证校验器
 * @date : 2023/12/15 10:50
 */
public class IdCardValidator implements ConstraintValidator<IdCard, String> {
    @Override
    public boolean isValid(String idCard, ConstraintValidatorContext constraintValidatorContext) {
        if (idCard == null) {
            return true;
        }
        return IdcardUtil.isValidCard(idCard);
    }
}
