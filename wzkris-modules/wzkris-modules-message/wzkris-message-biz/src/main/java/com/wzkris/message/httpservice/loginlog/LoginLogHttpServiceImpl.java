package com.wzkris.message.httpservice.loginlog;

import com.wzkris.common.core.enums.AuthTypeEnum;
import com.wzkris.message.domain.AdminLoginLogDO;
import com.wzkris.message.domain.TenantLoginLogDO;
import com.wzkris.message.httpservice.loginlog.req.LoginLogEvent;
import com.wzkris.message.mapper.AdminLoginLogMapper;
import com.wzkris.message.mapper.TenantLoginLogMapper;
import io.swagger.v3.oas.annotations.Hidden;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Hidden
@RestController
@RequiredArgsConstructor
public class LoginLogHttpServiceImpl implements LoginLogHttpService {

    private final AdminLoginLogMapper adminLoginLogMapper;

    private final TenantLoginLogMapper tenantLoginLogMapper;

    @Override
    public void save(@RequestBody List<LoginLogEvent> loginLogEvents) {
        if (CollectionUtils.isEmpty(loginLogEvents)) {
            return;
        }
        Map<String, List<LoginLogEvent>> listMap =
                loginLogEvents.stream().collect(Collectors.groupingBy(LoginLogEvent::getAuthType));
        saveAdminLogs(listMap.getOrDefault(AuthTypeEnum.ADMIN.getValue(), Collections.emptyList()));
        saveTenantLogs(listMap.getOrDefault(AuthTypeEnum.TENANT.getValue(), Collections.emptyList()));
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
            adminLoginLogDO.setSuccess(loginLogEvent.getSuccess());
            adminLoginLogDO.setErrorMsg(loginLogEvent.getErrorMsg());
            adminLoginLogDO.setBrowser(loginLogEvent.getBrowser());
            adminLoginLogDO.setOs(loginLogEvent.getOs());
            adminLoginLogDO.setLoginTime(loginLogEvent.getLoginTime());
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
            tenantLoginLogDO.setSuccess(loginLogEvent.getSuccess());
            tenantLoginLogDO.setErrorMsg(loginLogEvent.getErrorMsg());
            tenantLoginLogDO.setBrowser(loginLogEvent.getBrowser());
            tenantLoginLogDO.setOs(loginLogEvent.getOs());
            tenantLoginLogDO.setLoginTime(loginLogEvent.getLoginTime());
            loginLogs.add(tenantLoginLogDO);
        }
        tenantLoginLogMapper.insert(loginLogs, 1000);
    }

}

