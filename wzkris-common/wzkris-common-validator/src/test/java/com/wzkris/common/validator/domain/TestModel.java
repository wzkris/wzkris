package com.wzkris.common.validator.domain;

import com.wzkris.common.core.annotation.Sensitive;
import com.wzkris.common.core.enums.SensitiveStrategy;
import com.wzkris.common.validator.annotation.IdCard;
import com.wzkris.common.validator.annotation.PhoneNumber;
import lombok.Data;

@Data
public class TestModel {

    @IdCard
    private String idcard;

    @PhoneNumber
    private String phonenumber;

    @Sensitive(strategy = SensitiveStrategy.ADDRESS)
    private String ssAddr;

    @Sensitive(strategy = SensitiveStrategy.EMAIL)
    private String ssEmail;

    @Sensitive(strategy = SensitiveStrategy.PHONE)
    private String ssPhone;

    @Sensitive(strategy = SensitiveStrategy.BANK_CARD)
    private String ssBankcard;

}
