package com.wzkris.auth.oauth2.core.sms;

import com.wzkris.auth.oauth2.constants.OAuth2GrantTypeConstant;
import com.wzkris.auth.oauth2.core.CommonAuthenticationToken;
import com.wzkris.common.security.oauth2.enums.UserType;
import lombok.Getter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.Transient;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.util.Assert;

import java.util.Map;
import java.util.Set;


/**
 * @author wzkris
 * @date 2024/3/11
 * @description 短信验证token
 */
@Getter
@Transient
public final class SmsAuthenticationToken extends CommonAuthenticationToken {
    private final UserType userType;
    private final String phoneNumber;
    private final String smsCode;

    public SmsAuthenticationToken(UserType userType,
                                  String phoneNumber,
                                  String smsCode,
                                  Authentication clientPrincipal,
                                  Set<String> scopes,
                                  Map<String, Object> additionalParameters) {
        super(new AuthorizationGrantType(OAuth2GrantTypeConstant.SMS), clientPrincipal, scopes, additionalParameters);
        Assert.notNull(userType, "userType cannot be null");
        Assert.notNull(phoneNumber, "phoneNumber cannot be null");
        Assert.notNull(smsCode, "smsCode cannot be null");
        this.userType = userType;
        this.phoneNumber = phoneNumber;
        this.smsCode = smsCode;
    }
}
