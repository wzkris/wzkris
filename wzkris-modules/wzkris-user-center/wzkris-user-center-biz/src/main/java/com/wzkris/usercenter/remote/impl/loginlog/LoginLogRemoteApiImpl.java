package com.wzkris.usercenter.remote.impl.loginlog;

import com.wzkris.common.core.enums.AuthTypeEnum;
import com.wzkris.common.core.model.Result;
import com.wzkris.usercenter.domain.AdminLoginLogDO;
import com.wzkris.usercenter.domain.CustomerLoginLogDO;
import com.wzkris.usercenter.domain.TenantLoginLogDO;
import com.wzkris.usercenter.remote.api.loginlog.LoginLogRemoteApi;
import com.wzkris.usercenter.remote.api.loginlog.request.LoginLogEventRequest;
import com.wzkris.usercenter.service.AdminLoginLogService;
import com.wzkris.usercenter.service.CustomerLoginLogService;
import com.wzkris.usercenter.service.TenantLoginLogService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class LoginLogRemoteApiImpl implements LoginLogRemoteApi {

    private final AdminLoginLogService adminLoginLogService;

    private final TenantLoginLogService tenantLoginLogService;

    private final CustomerLoginLogService customerLoginLogService;

    @Override
    public Result<Void> save(List<LoginLogEventRequest> loginLogEventRequests) {
        if (CollectionUtils.isEmpty(loginLogEventRequests)) {
            return Result.ok();
        }
        Map<AuthTypeEnum, List<LoginLogEventRequest>> listMap =
                loginLogEventRequests.stream().collect(Collectors.groupingBy(LoginLogEventRequest::getAuthType));
        saveAdminLogs(listMap.getOrDefault(AuthTypeEnum.ADMIN, Collections.emptyList()));
        saveTenantLogs(listMap.getOrDefault(AuthTypeEnum.TENANT, Collections.emptyList()));
        saveCustomerLogs(listMap.getOrDefault(AuthTypeEnum.CUSTOMER, Collections.emptyList()));
        return Result.ok();
    }

    private void saveAdminLogs(List<LoginLogEventRequest> loginLogEventRequests) {
        if (CollectionUtils.isEmpty(loginLogEventRequests)) {
            return;
        }
        List<AdminLoginLogDO> loginLogs = new ArrayList<>();
        for (LoginLogEventRequest loginLogEventRequest : loginLogEventRequests) {
            AdminLoginLogDO adminLoginLogDO = new AdminLoginLogDO();
            adminLoginLogDO.setAdminId(loginLogEventRequest.getOperatorId());
            adminLoginLogDO.setUsername(loginLogEventRequest.getUsername());
            adminLoginLogDO.setLoginType(loginLogEventRequest.getLoginType());
            adminLoginLogDO.setLoginIp(loginLogEventRequest.getLoginIp());
            adminLoginLogDO.setLoginLocation(loginLogEventRequest.getLoginLocation());
            adminLoginLogDO.setTraceId(loginLogEventRequest.getTraceId());
            adminLoginLogDO.setUserAgent(loginLogEventRequest.getUserAgent());
            adminLoginLogDO.setSuccess(loginLogEventRequest.getSuccess());
            adminLoginLogDO.setErrorMsg(loginLogEventRequest.getErrorMsg());
            adminLoginLogDO.setLoginTime(loginLogEventRequest.getLoginTime());
            loginLogs.add(adminLoginLogDO);
        }
        adminLoginLogService.saveBatch(loginLogs, 1000);
    }

    private void saveTenantLogs(List<LoginLogEventRequest> loginLogEventRequests) {
        if (CollectionUtils.isEmpty(loginLogEventRequests)) {
            return;
        }
        List<TenantLoginLogDO> loginLogs = new ArrayList<>();
        for (LoginLogEventRequest loginLogEventRequest : loginLogEventRequests) {
            TenantLoginLogDO tenantLoginLogDO = new TenantLoginLogDO();
            tenantLoginLogDO.setMemberId(loginLogEventRequest.getOperatorId());
            tenantLoginLogDO.setUsername(loginLogEventRequest.getUsername());
            tenantLoginLogDO.setTenantId(loginLogEventRequest.getTenantId());
            tenantLoginLogDO.setLoginType(loginLogEventRequest.getLoginType());
            tenantLoginLogDO.setLoginIp(loginLogEventRequest.getLoginIp());
            tenantLoginLogDO.setLoginLocation(loginLogEventRequest.getLoginLocation());
            tenantLoginLogDO.setTraceId(loginLogEventRequest.getTraceId());
            tenantLoginLogDO.setUserAgent(loginLogEventRequest.getUserAgent());
            tenantLoginLogDO.setSuccess(loginLogEventRequest.getSuccess());
            tenantLoginLogDO.setErrorMsg(loginLogEventRequest.getErrorMsg());
            tenantLoginLogDO.setLoginTime(loginLogEventRequest.getLoginTime());
            loginLogs.add(tenantLoginLogDO);
        }
        tenantLoginLogService.saveBatch(loginLogs, 1000);
    }

    private void saveCustomerLogs(List<LoginLogEventRequest> loginLogEventRequests) {
        if (CollectionUtils.isEmpty(loginLogEventRequests)) {
            return;
        }
        List<CustomerLoginLogDO> loginLogs = new ArrayList<>();
        for (LoginLogEventRequest loginLogEventRequest : loginLogEventRequests) {
            CustomerLoginLogDO customerLoginLogDO = new CustomerLoginLogDO();
            customerLoginLogDO.setCustomerId(loginLogEventRequest.getOperatorId());
            customerLoginLogDO.setUsername(loginLogEventRequest.getUsername());
            customerLoginLogDO.setLoginType(loginLogEventRequest.getLoginType());
            customerLoginLogDO.setLoginIp(loginLogEventRequest.getLoginIp());
            customerLoginLogDO.setLoginLocation(loginLogEventRequest.getLoginLocation());
            customerLoginLogDO.setTraceId(loginLogEventRequest.getTraceId());
            customerLoginLogDO.setUserAgent(loginLogEventRequest.getUserAgent());
            customerLoginLogDO.setSuccess(loginLogEventRequest.getSuccess());
            customerLoginLogDO.setErrorMsg(loginLogEventRequest.getErrorMsg());
            customerLoginLogDO.setLoginTime(loginLogEventRequest.getLoginTime());
            loginLogs.add(customerLoginLogDO);
        }
        customerLoginLogService.saveBatch(loginLogs, 1000);
    }

}

