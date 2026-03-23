package com.wzkris.auth.listener;

import com.wzkris.auth.listener.event.LoginEvent;
import com.wzkris.auth.service.impl.LoginRiskAnalyzeService;
import com.wzkris.common.core.enums.AuthTypeEnum;
import com.wzkris.common.core.model.BaseLoginUser;
import com.wzkris.common.core.utils.IpUtil;
import com.wzkris.common.core.utils.JsonUtil;
import com.wzkris.common.core.utils.ResultUtil;
import com.wzkris.common.security.model.AdminLoginUser;
import com.wzkris.common.security.model.CustomerLoginUser;
import com.wzkris.common.security.model.TenantLoginUser;
import com.wzkris.system.httpclient.loginlog.LoginLogClient;
import com.wzkris.system.httpclient.loginlog.req.LoginLogEvent;
import com.wzkris.usercenter.httpclient.admin.AdminInfoClient;
import com.wzkris.usercenter.httpclient.admin.req.LoginInfoUpdateReq;
import com.wzkris.usercenter.httpclient.customer.CustomerInfoClient;
import com.wzkris.usercenter.httpclient.member.MemberInfoClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nl.basjes.parse.useragent.UserAgent;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.Date;

/**
 * @author : wzkris
 * @version : V1.0.0
 * @description : 登录事件监听
 * @date : 2023/8/28 10:05
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class LoginEventListener {

    private final LoginLogClient loginLogClient;

    private final LoginRiskAnalyzeService loginRiskAnalyzeService;

    private final AdminInfoClient adminInfoClient;

    private final MemberInfoClient memberInfoClient;

    private final CustomerInfoClient customerInfoClient;

    @Async
    @EventListener
    public void loginEvent(LoginEvent event) {
        final BaseLoginUser loginUser = event.getLoginUser();
        log.info("'{}' 发生登录事件", loginUser);

        AuthTypeEnum authType = loginUser.getAuthType();
        if (authType == AuthTypeEnum.ADMIN || authType == AuthTypeEnum.TENANT || authType == AuthTypeEnum.CUSTOMER) {
            this.handleLogin(event, loginUser);
        }
    }

    private void handleLogin(LoginEvent event, BaseLoginUser loginUser) {
        final String loginType = event.getLoginType();
        final String errorMsg = event.getErrorMsg();
        String ipAddr = event.getIpAddr();
        UserAgent userAgent = event.getUserAgent();
        String userAgentText = JsonUtil.toJsonString(userAgent.getHeaders());
        String loginLocation = IpUtil.parseIp(ipAddr);
        String traceId = event.getTraceId();
        Date now = new Date();
        LoginRiskAnalyzeService.RiskResult riskResult =
                loginRiskAnalyzeService.analyze(loginUser, ipAddr, userAgentText, event.getSuccess(), now);

        updateLoginInfoIfSuccess(loginUser, ipAddr, event.getSuccess(), now);

        LoginLogEvent loginLogEvent = new LoginLogEvent();
        loginLogEvent.setAuthType(loginUser.getAuthType().getValue());
        loginLogEvent.setOperatorId(loginUser.getUid());
        loginLogEvent.setUsername(resolveUsername(loginUser));
        loginLogEvent.setTenantId(resolveTenantId(loginUser));
        loginLogEvent.setLoginTime(now);
        loginLogEvent.setLoginIp(ipAddr);
        loginLogEvent.setLoginType(loginType);
        loginLogEvent.setSuccess(event.getSuccess());
        loginLogEvent.setErrorMsg(errorMsg);
        loginLogEvent.setLoginLocation(loginLocation);
        loginLogEvent.setTraceId(traceId);
        loginLogEvent.setUserAgent(userAgentText);
        loginLogEvent.setAbnormalTags(riskResult.abnormalTags());
        loginLogEvent.setRiskLevel(riskResult.riskLevel().getValue());
        loginLogEvent.setRiskScore(riskResult.riskScore());
        loginLogClient.save(Collections.singletonList(loginLogEvent));
        reportRiskAlertIfNecessary(loginUser, loginLogEvent, riskResult);
    }

    private void updateLoginInfoIfSuccess(BaseLoginUser loginUser, String ipAddr, Boolean success, Date loginDate) {
        if (!Boolean.TRUE.equals(success)) {
            return;
        }
        LoginInfoUpdateReq loginInfoUpdateReq = new LoginInfoUpdateReq(loginUser.getUid());
        loginInfoUpdateReq.setLoginIp(ipAddr);
        loginInfoUpdateReq.setLoginDate(loginDate);
        AuthTypeEnum authType = loginUser.getAuthType();
        if (authType == AuthTypeEnum.ADMIN) {
            ResultUtil.checkNoData(adminInfoClient.updateLoginInfo(loginInfoUpdateReq));
        } else if (authType == AuthTypeEnum.TENANT) {
            ResultUtil.checkNoData(memberInfoClient.updateLoginInfo(loginInfoUpdateReq));
        } else if (authType == AuthTypeEnum.CUSTOMER) {
            ResultUtil.checkNoData(customerInfoClient.updateLoginInfo(loginInfoUpdateReq));
        }
    }

    private String resolveUsername(BaseLoginUser loginUser) {
        if (loginUser instanceof AdminLoginUser admin) {
            return admin.getUsername();
        }
        if (loginUser instanceof TenantLoginUser tenant) {
            return tenant.getUsername();
        }
        if (loginUser instanceof CustomerLoginUser customer) {
            return customer.getPhoneNumber();
        }
        return String.valueOf(loginUser.getUid());
    }

    private Long resolveTenantId(BaseLoginUser loginUser) {
        if (loginUser instanceof TenantLoginUser tenant) {
            return tenant.getTenantId();
        }
        return null;
    }

    private void reportRiskAlertIfNecessary(BaseLoginUser loginUser, LoginLogEvent event, LoginRiskAnalyzeService.RiskResult riskResult) {
        if (!loginRiskAnalyzeService.shouldAlert(loginUser, riskResult)) {
            return;
        }
        log.warn("登录风险告警 authType={}, uid={}, riskLevel={}, score={}, tags={}, ip={}, traceId={}",
                loginUser.getAuthType(), loginUser.getUid(), riskResult.riskLevel(), riskResult.riskScore(),
                riskResult.abnormalTags(), event.getLoginIp(), event.getTraceId());
    }

}
