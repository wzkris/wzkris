package com.wzkris.auth.service;

import com.wzkris.auth.listener.event.LoginEvent;
import com.wzkris.auth.security.core.password.PasswordAuthenticationToken;
import com.wzkris.auth.security.core.sms.SmsAuthenticationToken;
import com.wzkris.common.core.enums.AuthTypeEnum;
import com.wzkris.common.core.model.BaseLoginUser;
import com.wzkris.common.core.model.Result;
import com.wzkris.common.core.utils.ResultUtil;
import com.wzkris.common.core.utils.ServletUtil;
import com.wzkris.common.core.utils.StringUtil;
import com.wzkris.risk.httpclient.captcha.CaptchaClient;
import com.wzkris.risk.httpclient.captcha.req.CaptchaSmsValidateReq;
import com.wzkris.risk.httpclient.riskctl.RiskClient;
import com.wzkris.risk.httpclient.riskctl.req.RiskDecideReq;
import com.wzkris.risk.httpclient.riskctl.req.RiskReportReq;
import com.wzkris.risk.httpclient.riskctl.resp.RiskDecisionResp;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Service
@RequiredArgsConstructor
public class AuthRiskFacade {

    private static final String SCENARIO_PASSWORD_LOGIN = "PASSWORD_LOGIN";

    private static final String SCENARIO_SMS_LOGIN = "SMS_LOGIN";

    private static final String SCENARIO_LOOKUP_FAILED = "LOOKUP_FAILED";

    private final RiskClient riskClient;

    private final CaptchaClient captchaClient;

    public RiskDecisionResp decidePassword(PasswordAuthenticationToken token) {
        RiskDecideReq req = buildDecideReq(
                SCENARIO_PASSWORD_LOGIN,
                token.getAuthType().getValue(),
                token.getUsername(),
                token.getCaptchaId());
        Result<RiskDecisionResp> result = riskClient.decide(req);
        if (!ResultUtil.check(result)) {
            return null;
        }
        return result.getData();
    }

    public RiskDecisionResp decideSms(SmsAuthenticationToken token) {
        RiskDecideReq req = buildDecideReq(
                SCENARIO_SMS_LOGIN,
                token.getAuthType().getValue(),
                token.getPhoneNumber(),
                null);
        Result<RiskDecisionResp> result = riskClient.decide(req);
        if (!ResultUtil.check(result)) {
            return null;
        }
        return result.getData();
    }

    public boolean validateSmsCode(String phone, String code) {
        CaptchaSmsValidateReq req = new CaptchaSmsValidateReq();
        req.setPhone(phone);
        req.setCode(code);
        Result<Boolean> result = captchaClient.validateSms(req);
        return ResultUtil.check(result) && Boolean.TRUE.equals(result.getData());
    }

    public void recordLookupFailed(AuthTypeEnum authType, String subject) {
        RiskReportReq req = new RiskReportReq();
        req.setScenario(SCENARIO_LOOKUP_FAILED);
        req.setAuthType(authType.getValue());
        req.setSubject(subject);
        req.setSuccess(false);
        fillRequestMeta(req);
        riskClient.report(req);
    }

    public void reportLoginEvent(LoginEvent event, BaseLoginUser loginUser, String subject) {
        RiskReportReq request = new RiskReportReq();
        request.setScenario(event.getLoginType());
        request.setAuthType(loginUser.getAuthType().getValue());
        request.setSubject(subject);
        request.setSuccess(event.getSuccess());
        request.setTraceId(event.getTraceId());
        request.setIpAddr(event.getIpAddr());
        request.setUserAgent(event.getUserAgent() == null ? null : event.getUserAgent().toString());
        riskClient.report(request);
    }

    private RiskDecideReq buildDecideReq(String scenario, String authType, String subject, String captchaId) {
        RiskDecideReq req = new RiskDecideReq();
        req.setScenario(scenario);
        req.setAuthType(authType);
        req.setSubject(subject);
        req.setCaptchaId(captchaId);
        fillRequestMeta(req);
        return req;
    }

    private void fillRequestMeta(RiskDecideReq req) {
        HttpServletRequest request = currentRequest();
        if (request == null) {
            return;
        }
        req.setIpAddr(ServletUtil.getClientIP(request));
        req.setUserAgent(request.getHeader(HttpHeaders.USER_AGENT));
        req.setOrigin(request.getHeader(HttpHeaders.ORIGIN));
        req.setReferer(request.getHeader(HttpHeaders.REFERER));
        req.setRequestPath(request.getRequestURI());
    }

    private void fillRequestMeta(RiskReportReq req) {
        HttpServletRequest request = currentRequest();
        if (request == null) {
            return;
        }
        if (StringUtil.isBlank(req.getIpAddr())) {
            req.setIpAddr(ServletUtil.getClientIP(request));
        }
        if (StringUtil.isBlank(req.getUserAgent())) {
            req.setUserAgent(request.getHeader(HttpHeaders.USER_AGENT));
        }
    }

    private HttpServletRequest currentRequest() {
        if (!(RequestContextHolder.getRequestAttributes() instanceof ServletRequestAttributes servletRequestAttributes)) {
            return null;
        }
        return servletRequestAttributes.getRequest();
    }

}
