package com.wzkris.system.httpclient.loginlog;

import com.wzkris.common.core.model.Result;
import com.wzkris.common.core.enums.AuthTypeEnum;
import com.wzkris.system.domain.AdminLoginLogDO;
import com.wzkris.system.domain.TenantLoginLogDO;
import com.wzkris.system.httpclient.loginlog.req.LoginLogEvent;
import com.wzkris.system.mapper.AdminLoginLogMapper;
import com.wzkris.system.mapper.TenantLoginLogMapper;
import io.swagger.v3.oas.annotations.Hidden;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Hidden
@RestController
@RequiredArgsConstructor
public class LoginLogClientImpl implements LoginLogClient {

    private final AdminLoginLogMapper adminLoginLogMapper;

    private final TenantLoginLogMapper tenantLoginLogMapper;

    @Override
    public Result<Void> save(@RequestBody List<LoginLogEvent> loginLogEvents) {
        if (CollectionUtils.isEmpty(loginLogEvents)) {
            return Result.ok();
        }
        Map<String, List<LoginLogEvent>> listMap =
                loginLogEvents.stream().collect(Collectors.groupingBy(LoginLogEvent::getAuthType));
        saveAdminLogs(listMap.getOrDefault(AuthTypeEnum.ADMIN.getValue(), Collections.emptyList()));
        saveAdminLogs(listMap.getOrDefault(AuthTypeEnum.CUSTOMER.getValue(), Collections.emptyList()));
        saveTenantLogs(listMap.getOrDefault(AuthTypeEnum.TENANT.getValue(), Collections.emptyList()));
        return Result.ok();
    }

    private void saveAdminLogs(List<LoginLogEvent> loginLogEvents) {
        if (CollectionUtils.isEmpty(loginLogEvents)) {
            return;
        }
        List<AdminLoginLogDO> loginLogs = new ArrayList<>();
        for (LoginLogEvent loginLogEvent : loginLogEvents) {
            AdminLoginLogDO adminLoginLogDO = new AdminLoginLogDO();
            adminLoginLogDO.setAdminId(loginLogEvent.getOperatorId());
            adminLoginLogDO.setUsername(loginLogEvent.getUsername());
            adminLoginLogDO.setLoginType(loginLogEvent.getLoginType());
            adminLoginLogDO.setLoginIp(loginLogEvent.getLoginIp());
            adminLoginLogDO.setLoginLocation(loginLogEvent.getLoginLocation());
            adminLoginLogDO.setTraceId(loginLogEvent.getTraceId());
            adminLoginLogDO.setUserAgent(loginLogEvent.getUserAgent());
            adminLoginLogDO.setSuccess(loginLogEvent.getSuccess());
            adminLoginLogDO.setErrorMsg(loginLogEvent.getErrorMsg());
            adminLoginLogDO.setLoginTime(loginLogEvent.getLoginTime());
            adminLoginLogDO.setAbnormalTags(loginLogEvent.getAbnormalTags());
            adminLoginLogDO.setRiskLevel(loginLogEvent.getRiskLevel());
            adminLoginLogDO.setRiskScore(loginLogEvent.getRiskScore());
            loginLogs.add(adminLoginLogDO);
        }
        adminLoginLogMapper.insert(loginLogs, 1000);
    }

    private void saveTenantLogs(List<LoginLogEvent> loginLogEvents) {
        if (CollectionUtils.isEmpty(loginLogEvents)) {
            return;
        }
        List<TenantLoginLogDO> loginLogs = new ArrayList<>();
        for (LoginLogEvent loginLogEvent : loginLogEvents) {
            TenantLoginLogDO tenantLoginLogDO = new TenantLoginLogDO();
            tenantLoginLogDO.setMemberId(loginLogEvent.getOperatorId());
            tenantLoginLogDO.setUsername(loginLogEvent.getUsername());
            tenantLoginLogDO.setTenantId(loginLogEvent.getTenantId());
            tenantLoginLogDO.setLoginType(loginLogEvent.getLoginType());
            tenantLoginLogDO.setLoginIp(loginLogEvent.getLoginIp());
            tenantLoginLogDO.setLoginLocation(loginLogEvent.getLoginLocation());
            tenantLoginLogDO.setTraceId(loginLogEvent.getTraceId());
            tenantLoginLogDO.setUserAgent(loginLogEvent.getUserAgent());
            tenantLoginLogDO.setSuccess(loginLogEvent.getSuccess());
            tenantLoginLogDO.setErrorMsg(loginLogEvent.getErrorMsg());
            tenantLoginLogDO.setLoginTime(loginLogEvent.getLoginTime());
            tenantLoginLogDO.setAbnormalTags(loginLogEvent.getAbnormalTags());
            tenantLoginLogDO.setRiskLevel(loginLogEvent.getRiskLevel());
            tenantLoginLogDO.setRiskScore(loginLogEvent.getRiskScore());
            loginLogs.add(tenantLoginLogDO);
        }
        tenantLoginLogMapper.insert(loginLogs, 1000);
    }

}

