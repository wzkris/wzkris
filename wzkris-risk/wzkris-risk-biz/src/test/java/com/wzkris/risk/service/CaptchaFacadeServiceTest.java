package com.wzkris.risk.service;

import com.wzkris.common.core.model.Result;
import com.wzkris.risk.httpclient.captcha.req.CaptchaSmsCodeReq;
import com.wzkris.risk.service.captcha.CaptchaFacadeService;
import com.wzkris.risk.service.captcha.CaptchaService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CaptchaFacadeServiceTest {

    @Mock
    private CaptchaService captchaService;

    private CaptchaFacadeService captchaFacadeService;

    @BeforeEach
    void setUp() {
        captchaFacadeService = new CaptchaFacadeService(captchaService);
    }

    @Test
    void sendSms_shouldFailWhenCaptchaInvalid() {
        CaptchaSmsCodeReq req = new CaptchaSmsCodeReq();
        req.setPhone("13800000000");
        req.setCaptchaId("invalid-token");
        when(captchaService.validateChallenge("invalid-token")).thenReturn(false);

        Result<Integer> result = captchaFacadeService.sendSms(req);

        assertNotNull(result);
        assertEquals(400, result.getCode());
        verify(captchaService, never()).setCaptcha(anyString(), anyString());
    }

    @Test
    void sendSms_shouldGenerateAndStoreCodeWhenCaptchaValid() {
        CaptchaSmsCodeReq req = new CaptchaSmsCodeReq();
        req.setPhone("13800000000");
        req.setCaptchaId("ok-token");
        when(captchaService.validateChallenge("ok-token")).thenReturn(true);

        Result<Integer> result = captchaFacadeService.sendSms(req);

        assertNotNull(result);
        assertEquals(200, result.getCode());
        verify(captchaService).validateSmsMaxTry("13800000000", 1, 120);
        verify(captchaService).setCaptcha(org.mockito.ArgumentMatchers.eq("13800000000"), anyString());
    }

}
