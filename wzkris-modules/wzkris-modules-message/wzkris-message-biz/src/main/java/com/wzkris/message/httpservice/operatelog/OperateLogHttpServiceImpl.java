package com.wzkris.message.httpservice.operatelog;

import com.wzkris.common.core.enums.AuthTypeEnum;
import com.wzkris.message.domain.AdminOperateLogDO;
import com.wzkris.message.domain.TenantOperateLogDO;
import com.wzkris.message.httpservice.operatelog.req.OperateLogEvent;
import com.wzkris.message.mapper.AdminOperateLogMapper;
import com.wzkris.message.mapper.TenantOperateLogMapper;
import io.swagger.v3.oas.annotations.Hidden;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Hidden
@RestController
@RequiredArgsConstructor
public class OperateLogHttpServiceImpl implements OperateLogHttpService {

    private final AdminOperateLogMapper adminOperateLogMapper;

    private final TenantOperateLogMapper tenantOperateLogMapper;

    @Override
    public void save(List<OperateLogEvent> operateLogEvents) {
        if (CollectionUtils.isEmpty(operateLogEvents)) {
            return;
        }
        Map<String, List<OperateLogEvent>> listMap =
                operateLogEvents.stream()
                        .collect(Collectors.groupingBy(OperateLogEvent::getAuthType));
        saveAdminLogs(listMap.getOrDefault(AuthTypeEnum.ADMIN.getValue(), Collections.emptyList()));
        saveTenantLogs(listMap.getOrDefault(AuthTypeEnum.TENANT.getValue(), Collections.emptyList()));
    }

    private void saveAdminLogs(List<OperateLogEvent> operateLogEvents) {
        if (CollectionUtils.isEmpty(operateLogEvents)) {
            return;
        }
        List<AdminOperateLogDO> operLogs = new ArrayList<>();
        for (OperateLogEvent operateLogEvent : operateLogEvents) {
            AdminOperateLogDO adminOperateLogDO = new AdminOperateLogDO();
            adminOperateLogDO.setTitle(operateLogEvent.getTitle());
            adminOperateLogDO.setSubTitle(operateLogEvent.getSubTitle());
            adminOperateLogDO.setOperType(operateLogEvent.getOperType());
            adminOperateLogDO.setMethod(operateLogEvent.getMethod());
            adminOperateLogDO.setRequestMethod(operateLogEvent.getRequestMethod());
            adminOperateLogDO.setAdminId(operateLogEvent.getOperatorId());
            adminOperateLogDO.setUsername(operateLogEvent.getOperName());
            adminOperateLogDO.setOperUrl(operateLogEvent.getOperUrl());
            adminOperateLogDO.setOperIp(operateLogEvent.getOperIp());
            adminOperateLogDO.setOperParam(operateLogEvent.getOperParam());
            adminOperateLogDO.setJsonResult(operateLogEvent.getJsonResult());
            adminOperateLogDO.setOperLocation(operateLogEvent.getOperLocation());
            adminOperateLogDO.setSuccess(operateLogEvent.getSuccess());
            adminOperateLogDO.setErrorMsg(operateLogEvent.getErrorMsg());
            adminOperateLogDO.setOperTime(operateLogEvent.getOperTime());

            operLogs.add(adminOperateLogDO);
        }
        adminOperateLogMapper.insert(operLogs, 1000);
    }

    private void saveTenantLogs(List<OperateLogEvent> operateLogEvents) {
        if (CollectionUtils.isEmpty(operateLogEvents)) {
            return;
        }

        List<TenantOperateLogDO> operLogs = new ArrayList<>();
        for (OperateLogEvent operateLogEvent : operateLogEvents) {
            TenantOperateLogDO tenantOperateLogDO = new TenantOperateLogDO();
            tenantOperateLogDO.setTitle(operateLogEvent.getTitle());
            tenantOperateLogDO.setSubTitle(operateLogEvent.getSubTitle());
            tenantOperateLogDO.setOperType(operateLogEvent.getOperType());
            tenantOperateLogDO.setMethod(operateLogEvent.getMethod());
            tenantOperateLogDO.setRequestMethod(operateLogEvent.getRequestMethod());
            tenantOperateLogDO.setMemberId(operateLogEvent.getOperatorId());
            tenantOperateLogDO.setUsername(operateLogEvent.getOperName());
            tenantOperateLogDO.setOperUrl(operateLogEvent.getOperUrl());
            tenantOperateLogDO.setOperIp(operateLogEvent.getOperIp());
            tenantOperateLogDO.setOperParam(operateLogEvent.getOperParam());
            tenantOperateLogDO.setJsonResult(operateLogEvent.getJsonResult());
            tenantOperateLogDO.setOperLocation(operateLogEvent.getOperLocation());
            tenantOperateLogDO.setSuccess(operateLogEvent.getSuccess());
            tenantOperateLogDO.setErrorMsg(operateLogEvent.getErrorMsg());
            tenantOperateLogDO.setOperTime(operateLogEvent.getOperTime());
            tenantOperateLogDO.setTenantId(operateLogEvent.getTenantId());

            operLogs.add(tenantOperateLogDO);
        }
        tenantOperateLogMapper.insert(operLogs, 1000);
    }

}
