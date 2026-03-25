package com.wzkris.system.remote.impl.operatelog;

import com.wzkris.common.core.enums.AuthTypeEnum;
import com.wzkris.common.core.model.Result;
import com.wzkris.system.domain.AdminOperateLogDO;
import com.wzkris.system.domain.TenantOperateLogDO;
import com.wzkris.system.mapper.AdminOperateLogMapper;
import com.wzkris.system.mapper.TenantOperateLogMapper;
import com.wzkris.system.remote.api.operatelog.OperateLogRemoteApi;
import com.wzkris.system.remote.api.operatelog.request.OperateLogEventRequest;
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
public class OperateLogRemoteApiImpl implements OperateLogRemoteApi {

    private final AdminOperateLogMapper adminOperateLogMapper;

    private final TenantOperateLogMapper tenantOperateLogMapper;

    @Override
    public Result<Void> save(List<OperateLogEventRequest> operateLogEventRequests) {
        if (CollectionUtils.isEmpty(operateLogEventRequests)) {
            return Result.ok();
        }
        Map<String, List<OperateLogEventRequest>> listMap =
                operateLogEventRequests.stream()
                        .collect(Collectors.groupingBy(OperateLogEventRequest::getAuthType));
        saveAdminLogs(listMap.getOrDefault(AuthTypeEnum.ADMIN.getValue(), Collections.emptyList()));
        saveTenantLogs(listMap.getOrDefault(AuthTypeEnum.TENANT.getValue(), Collections.emptyList()));
        return Result.ok();
    }

    private void saveAdminLogs(List<OperateLogEventRequest> operateLogEventRequests) {
        if (CollectionUtils.isEmpty(operateLogEventRequests)) {
            return;
        }
        List<AdminOperateLogDO> operLogs = new ArrayList<>();
        for (OperateLogEventRequest operateLogEventRequest : operateLogEventRequests) {
            AdminOperateLogDO adminOperateLogDO = new AdminOperateLogDO();
            adminOperateLogDO.setTitle(operateLogEventRequest.getTitle());
            adminOperateLogDO.setSubTitle(operateLogEventRequest.getSubTitle());
            adminOperateLogDO.setOperType(operateLogEventRequest.getOperType());
            adminOperateLogDO.setMethod(operateLogEventRequest.getMethod());
            adminOperateLogDO.setRequestMethod(operateLogEventRequest.getRequestMethod());
            adminOperateLogDO.setAdminId(operateLogEventRequest.getOperatorId());
            adminOperateLogDO.setUsername(operateLogEventRequest.getOperName());
            adminOperateLogDO.setOperUrl(operateLogEventRequest.getOperUrl());
            adminOperateLogDO.setOperIp(operateLogEventRequest.getOperIp());
            adminOperateLogDO.setOperParam(operateLogEventRequest.getOperParam());
            adminOperateLogDO.setJsonResult(operateLogEventRequest.getJsonResult());
            adminOperateLogDO.setOperLocation(operateLogEventRequest.getOperLocation());
            adminOperateLogDO.setSuccess(operateLogEventRequest.getSuccess());
            adminOperateLogDO.setErrorMsg(operateLogEventRequest.getErrorMsg());
            adminOperateLogDO.setOperTime(operateLogEventRequest.getOperTime());
            operLogs.add(adminOperateLogDO);
        }
        adminOperateLogMapper.insert(operLogs, 1000);
    }

    private void saveTenantLogs(List<OperateLogEventRequest> operateLogEventRequests) {
        if (CollectionUtils.isEmpty(operateLogEventRequests)) {
            return;
        }
        List<TenantOperateLogDO> operLogs = new ArrayList<>();
        for (OperateLogEventRequest operateLogEventRequest : operateLogEventRequests) {
            TenantOperateLogDO tenantOperateLogDO = new TenantOperateLogDO();
            tenantOperateLogDO.setTitle(operateLogEventRequest.getTitle());
            tenantOperateLogDO.setSubTitle(operateLogEventRequest.getSubTitle());
            tenantOperateLogDO.setOperType(operateLogEventRequest.getOperType());
            tenantOperateLogDO.setMethod(operateLogEventRequest.getMethod());
            tenantOperateLogDO.setRequestMethod(operateLogEventRequest.getRequestMethod());
            tenantOperateLogDO.setMemberId(operateLogEventRequest.getOperatorId());
            tenantOperateLogDO.setUsername(operateLogEventRequest.getOperName());
            tenantOperateLogDO.setOperUrl(operateLogEventRequest.getOperUrl());
            tenantOperateLogDO.setOperIp(operateLogEventRequest.getOperIp());
            tenantOperateLogDO.setOperParam(operateLogEventRequest.getOperParam());
            tenantOperateLogDO.setJsonResult(operateLogEventRequest.getJsonResult());
            tenantOperateLogDO.setOperLocation(operateLogEventRequest.getOperLocation());
            tenantOperateLogDO.setSuccess(operateLogEventRequest.getSuccess());
            tenantOperateLogDO.setErrorMsg(operateLogEventRequest.getErrorMsg());
            tenantOperateLogDO.setOperTime(operateLogEventRequest.getOperTime());
            tenantOperateLogDO.setTenantId(operateLogEventRequest.getTenantId());
            operLogs.add(tenantOperateLogDO);
        }
        tenantOperateLogMapper.insert(operLogs, 1000);
    }

}

