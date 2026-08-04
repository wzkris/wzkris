package com.wzkris.usercenter.remote.impl.operatelog;

import com.wzkris.common.core.enums.AuthTypeEnum;
import com.wzkris.common.core.model.Result;
import com.wzkris.usercenter.domain.AdminOperateLogDO;
import com.wzkris.usercenter.domain.CustomerOperateLogDO;
import com.wzkris.usercenter.domain.TenantOperateLogDO;
import com.wzkris.usercenter.remote.api.operatelog.OperateLogRemoteApi;
import com.wzkris.usercenter.remote.api.operatelog.request.OperateLogEventRequest;
import com.wzkris.usercenter.service.AdminOperateLogService;
import com.wzkris.usercenter.service.CustomerOperateLogService;
import com.wzkris.usercenter.service.TenantOperateLogService;
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

    private final AdminOperateLogService adminOperateLogService;

    private final TenantOperateLogService tenantOperateLogService;

    private final CustomerOperateLogService customerOperateLogService;

    @Override
    public Result<Void> save(List<OperateLogEventRequest> requestList) {
        if (CollectionUtils.isEmpty(requestList)) {
            return Result.ok();
        }
        // authType 为空表示无登录用户上下文（定时任务、异步、系统自动操作等普通方法），
        // 此类日志按约定不落库；同时避免 Collectors.groupingBy 对 null 键抛 NPE 拖垮整批
        Map<AuthTypeEnum, List<OperateLogEventRequest>> listMap = requestList.stream()
                .filter(req -> req.getAuthType() != null)
                .collect(Collectors.groupingBy(OperateLogEventRequest::getAuthType));
        saveAdminLogs(listMap.getOrDefault(AuthTypeEnum.ADMIN, Collections.emptyList()));
        saveTenantLogs(listMap.getOrDefault(AuthTypeEnum.TENANT, Collections.emptyList()));
        saveCustomerLogs(listMap.getOrDefault(AuthTypeEnum.CUSTOMER, Collections.emptyList()));
        return Result.ok();
    }

    private void saveAdminLogs(List<OperateLogEventRequest> requestList) {
        if (CollectionUtils.isEmpty(requestList)) {
            return;
        }
        List<AdminOperateLogDO> operLogs = new ArrayList<>();
        for (OperateLogEventRequest request : requestList) {
            AdminOperateLogDO adminOperateLogDO = new AdminOperateLogDO();
            adminOperateLogDO.setTitle(request.getTitle());
            adminOperateLogDO.setSubTitle(request.getSubTitle());
            adminOperateLogDO.setOperType(request.getOperType());
            adminOperateLogDO.setMethod(request.getMethod());
            adminOperateLogDO.setHttpMethod(request.getHttpMethod());
            adminOperateLogDO.setAdminId(request.getOperatorId());
            adminOperateLogDO.setUsername(request.getOperName());
            adminOperateLogDO.setHttpUrl(request.getHttpUrl());
            adminOperateLogDO.setOperIp(request.getOperIp());
            adminOperateLogDO.setOperParam(request.getOperParam());
            adminOperateLogDO.setJsonResult(request.getJsonResult());
            adminOperateLogDO.setOperLocation(request.getOperLocation());
            adminOperateLogDO.setSuccess(request.getSuccess());
            adminOperateLogDO.setErrorMsg(request.getErrorMsg());
            adminOperateLogDO.setOperTime(request.getOperTime());
            adminOperateLogDO.setCostTime(request.getCostTime());
            operLogs.add(adminOperateLogDO);
        }
        adminOperateLogService.saveBatch(operLogs, 1000);
    }

    private void saveTenantLogs(List<OperateLogEventRequest> requestList) {
        if (CollectionUtils.isEmpty(requestList)) {
            return;
        }
        List<TenantOperateLogDO> operLogs = new ArrayList<>();
        for (OperateLogEventRequest request : requestList) {
            TenantOperateLogDO tenantOperateLogDO = new TenantOperateLogDO();
            tenantOperateLogDO.setTitle(request.getTitle());
            tenantOperateLogDO.setSubTitle(request.getSubTitle());
            tenantOperateLogDO.setOperType(request.getOperType());
            tenantOperateLogDO.setMethod(request.getMethod());
            tenantOperateLogDO.setHttpMethod(request.getHttpMethod());
            tenantOperateLogDO.setMemberId(request.getOperatorId());
            tenantOperateLogDO.setUsername(request.getOperName());
            tenantOperateLogDO.setHttpUrl(request.getHttpUrl());
            tenantOperateLogDO.setOperIp(request.getOperIp());
            tenantOperateLogDO.setOperParam(request.getOperParam());
            tenantOperateLogDO.setJsonResult(request.getJsonResult());
            tenantOperateLogDO.setOperLocation(request.getOperLocation());
            tenantOperateLogDO.setSuccess(request.getSuccess());
            tenantOperateLogDO.setErrorMsg(request.getErrorMsg());
            tenantOperateLogDO.setOperTime(request.getOperTime());
            tenantOperateLogDO.setCostTime(request.getCostTime());
            tenantOperateLogDO.setTenantId(request.getTenantId());
            operLogs.add(tenantOperateLogDO);
        }
        tenantOperateLogService.saveBatch(operLogs, 1000);
    }

    private void saveCustomerLogs(List<OperateLogEventRequest> requestList) {
        if (CollectionUtils.isEmpty(requestList)) {
            return;
        }
        List<CustomerOperateLogDO> operLogs = new ArrayList<>();
        for (OperateLogEventRequest request : requestList) {
            CustomerOperateLogDO customerOperateLogDO = new CustomerOperateLogDO();
            customerOperateLogDO.setTitle(request.getTitle());
            customerOperateLogDO.setSubTitle(request.getSubTitle());
            customerOperateLogDO.setOperType(request.getOperType());
            customerOperateLogDO.setMethod(request.getMethod());
            customerOperateLogDO.setHttpMethod(request.getHttpMethod());
            customerOperateLogDO.setCustomerId(request.getOperatorId());
            customerOperateLogDO.setUsername(request.getOperName());
            customerOperateLogDO.setHttpUrl(request.getHttpUrl());
            customerOperateLogDO.setOperIp(request.getOperIp());
            customerOperateLogDO.setOperParam(request.getOperParam());
            customerOperateLogDO.setJsonResult(request.getJsonResult());
            customerOperateLogDO.setOperLocation(request.getOperLocation());
            customerOperateLogDO.setSuccess(request.getSuccess());
            customerOperateLogDO.setErrorMsg(request.getErrorMsg());
            customerOperateLogDO.setOperTime(request.getOperTime());
            customerOperateLogDO.setCostTime(request.getCostTime());
            operLogs.add(customerOperateLogDO);
        }
        customerOperateLogService.saveBatch(operLogs, 1000);
    }

}

