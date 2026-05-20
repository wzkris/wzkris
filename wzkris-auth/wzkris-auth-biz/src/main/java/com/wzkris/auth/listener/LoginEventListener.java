package com.wzkris.auth.listener;

import com.wzkris.auth.event.LoginEvent;
import com.wzkris.auth.remote.interfaces.admin.IAdminInfoRemote;
import com.wzkris.auth.remote.interfaces.admin.request.LoginInfoUpdateRequest;
import com.wzkris.auth.remote.interfaces.customer.ICustomerInfoRemote;
import com.wzkris.auth.remote.interfaces.loginlog.ILoginLogRemote;
import com.wzkris.auth.remote.interfaces.loginlog.request.LoginLogEvent;
import com.wzkris.auth.remote.interfaces.member.IMemberInfoRemote;
import com.wzkris.auth.service.LoginRiskAnalyzeService;
import com.wzkris.common.core.enums.AuthTypeEnum;
import com.wzkris.common.core.model.BaseLoginUser;
import com.wzkris.common.core.utils.IpUtil;
import com.wzkris.common.core.utils.ResultUtil;
import com.wzkris.common.security.model.LoginAdminUser;
import com.wzkris.common.security.model.LoginCustomerUser;
import com.wzkris.common.security.model.LoginTenantUser;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.time.OffsetDateTime;
import java.util.Collections;

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

    private final ILoginLogRemote loginLogRemote;

    private final LoginRiskAnalyzeService loginRiskAnalyzeService;

    private final IAdminInfoRemote adminInfoRemote;

    private final IMemberInfoRemote memberInfoRemote;

    private final ICustomerInfoRemote customerInfoRemote;

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
        String userAgentText = event.getUserAgentText();
        String loginLocation = IpUtil.parseIp(ipAddr);
        String traceId = event.getTraceId();
        OffsetDateTime now = OffsetDateTime.now();
        LoginRiskAnalyzeService.RiskResult riskResult =
                loginRiskAnalyzeService.analyze(loginUser, ipAddr, userAgentText, event.getSuccess(), now);

        updateLoginInfoIfSuccess(loginUser, ipAddr, event.getSuccess(), now);

        LoginLogEvent loginLogEvent = new LoginLogEvent();
        loginLogEvent.setAuthType(loginUser.getAuthType());
        loginLogEvent.setOperatorId(loginUser.getUid());
        loginLogEvent.setActorUid(loginUser.getActorUid());
        loginLogEvent.setActorAuthType(loginUser.getActorAuthType());
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
        loginLogEvent.setRiskLevel(riskResult.riskLevel());
        loginLogEvent.setRiskScore(riskResult.riskScore());
        loginLogRemote.save(Collections.singletonList(loginLogEvent));
        reportRiskAlertIfNecessary(loginUser, loginLogEvent, riskResult);
    }

    private void updateLoginInfoIfSuccess(BaseLoginUser loginUser, String ipAddr, Boolean success, OffsetDateTime loginDate) {
        if (!Boolean.TRUE.equals(success)) {
            return;
        }
        LoginInfoUpdateRequest LoginInfoUpdateRequest = new LoginInfoUpdateRequest(loginUser.getUid());
        LoginInfoUpdateRequest.setLoginIp(ipAddr);
        LoginInfoUpdateRequest.setLoginDate(loginDate);
        AuthTypeEnum authType = loginUser.getAuthType();
        if (authType == AuthTypeEnum.ADMIN) {
            ResultUtil.checkNoData(adminInfoRemote.updateLoginInfo(LoginInfoUpdateRequest));
        } else if (authType == AuthTypeEnum.TENANT) {
            ResultUtil.checkNoData(memberInfoRemote.updateLoginInfo(LoginInfoUpdateRequest));
        } else if (authType == AuthTypeEnum.CUSTOMER) {
            ResultUtil.checkNoData(customerInfoRemote.updateLoginInfo(LoginInfoUpdateRequest));
        }
    }

    private String resolveUsername(BaseLoginUser loginUser) {
        if (loginUser instanceof LoginAdminUser adminUser) {
            return adminUser.getUsername();
        }
        if (loginUser instanceof LoginTenantUser tenantUser) {
            return tenantUser.getUsername();
        }
        if (loginUser instanceof LoginCustomerUser customerUser) {
            return customerUser.getPhoneNumber();
        }
        return String.valueOf(loginUser.getUid());
    }

    private Long resolveTenantId(BaseLoginUser loginUser) {
        if (loginUser instanceof LoginTenantUser tenantUser) {
            return tenantUser.getTenantId();
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

