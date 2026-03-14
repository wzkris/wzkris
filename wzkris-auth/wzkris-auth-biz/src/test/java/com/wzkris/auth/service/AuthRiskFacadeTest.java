package com.wzkris.auth.service;

import com.wzkris.common.core.model.Result;
import com.wzkris.risk.httpclient.captcha.CaptchaClient;
import com.wzkris.risk.httpclient.captcha.req.CaptchaSmsValidateReq;
import com.wzkris.risk.httpclient.riskctl.RiskClient;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuthRiskFacadeTest {

    @Mock
    private RiskClient riskClient;

    @Mock
    private CaptchaClient captchaClient;

    @InjectMocks
    private AuthRiskFacade authRiskFacade;

    @Test
    void validateSmsCode_shouldReturnTrueWhenRiskServicePasses() {
        when(captchaClient.validateSms(org.mockito.ArgumentMatchers.any()))
                .thenReturn(Result.ok(true));

        boolean pass = authRiskFacade.validateSmsCode("13800138000", "123456");
        assertTrue(pass);

        ArgumentCaptor<CaptchaSmsValidateReq> captor = ArgumentCaptor.forClass(CaptchaSmsValidateReq.class);
        verify(captchaClient).validateSms(captor.capture());
        CaptchaSmsValidateReq req = captor.getValue();
        assertTrue("13800138000".equals(req.getPhone()));
        assertTrue("123456".equals(req.getCode()));
    }

    @Test
    void validateSmsCode_shouldReturnFalseWhenRiskServiceRejects() {
        when(captchaClient.validateSms(org.mockito.ArgumentMatchers.any()))
                .thenReturn(Result.init(1, false, "invalid"));

        boolean pass = authRiskFacade.validateSmsCode("13800138000", "bad");
        assertFalse(pass);
    }
}
