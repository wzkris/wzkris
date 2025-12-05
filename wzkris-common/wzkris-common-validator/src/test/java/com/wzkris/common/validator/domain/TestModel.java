package com.wzkris.common.validator.domain;

import com.wzkris.common.core.annotation.Sensitive;
import com.wzkris.common.core.enums.SensitiveStrategyEnum;
import com.wzkris.common.validator.annotation.IdCard;
import com.wzkris.common.validator.annotation.PhoneNumber;
import lombok.Data;

@Data
public class TestModel {

    @IdCard
    private String idcard;

    @PhoneNumber
    private String phonenumber;

    @Sensitive(strategy = SensitiveStrategyEnum.ADDRESS)
    private String ssAddr;

    @Sensitive(strategy = SensitiveStrategyEnum.EMAIL)
    private String ssEmail;

    @Sensitive(strategy = SensitiveStrategyEnum.PHONE)
    private String ssPhone;

    @Sensitive(strategy = SensitiveStrategyEnum.BANK_CARD)
    private String ssBankcard;

}
