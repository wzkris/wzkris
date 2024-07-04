package com.wzkris.auth.oauth2.authenticate.sms;

import com.wzkris.auth.oauth2.authenticate.CommonAuthenticationToken;
import com.wzkris.auth.oauth2.constants.GrantTypeConstant;
import lombok.Getter;
import org.springframework.security.core.Authentication;
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
public class SmsAuthenticationToken extends CommonAuthenticationToken {
    private final String phoneNumber;
    private final String smsCode;

    public SmsAuthenticationToken(String phoneNumber,
                                  String smsCode,
                                  Authentication clientPrincipal,
                                  Set<String> scopes,
                                  Map<String, Object> additionalParameters) {
        super(new AuthorizationGrantType(GrantTypeConstant.SMS), clientPrincipal, scopes, additionalParameters);
        Assert.notNull(phoneNumber, "phoneNumber cannot be null");
        Assert.notNull(smsCode, "smsCode cannot be null");
        this.phoneNumber = phoneNumber;
        this.smsCode = smsCode;
    }
}
