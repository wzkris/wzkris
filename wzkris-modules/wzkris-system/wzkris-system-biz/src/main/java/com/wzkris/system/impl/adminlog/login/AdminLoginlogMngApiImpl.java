package com.wzkris.system.impl.adminlog.login;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.wzkris.common.core.enums.RiskLevelEnum;
import com.wzkris.common.core.model.Result;
import com.wzkris.common.core.utils.StringUtil;
import com.wzkris.common.orm.model.AbstractApi;
import com.wzkris.common.orm.model.Page;
import com.wzkris.common.web.utils.BeanUtil;
import com.wzkris.system.api.adminlog.login.AdminLoginlogMngApi;
import com.wzkris.system.domain.AdminLoginLogDO;
import com.wzkris.system.request.adminlog.AdminLoginLogMngQueryRequest;
import com.wzkris.system.response.adminlog.AdminLoginLogMngResponse;
import com.wzkris.system.service.AdminLoginLogService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminLoginlogMngApiImpl extends AbstractApi implements AdminLoginlogMngApi {

    private final AdminLoginLogService adminLoginLogService;

    @Override
    public Result<Page<AdminLoginLogMngResponse>> queryPage(AdminLoginLogMngQueryRequest request) {
        startPage();
        List<AdminLoginLogDO> list = adminLoginLogService.list(buildQueryWrapper(request));
        return getPageResult(BeanUtil.convert(list, AdminLoginLogMngResponse.class));
    }

    private LambdaQueryWrapper<AdminLoginLogDO> buildQueryWrapper(AdminLoginLogMngQueryRequest request) {
        return new LambdaQueryWrapper<AdminLoginLogDO>()
                .eq(ObjectUtils.isNotEmpty(request.getAdminId()), AdminLoginLogDO::getAdminId, request.getAdminId())
                .eq(ObjectUtils.isNotEmpty(request.getSuccess()), AdminLoginLogDO::getSuccess, request.getSuccess())
                .eq(StringUtil.isNotEmpty(request.getTraceId()), AdminLoginLogDO::getTraceId, request.getTraceId())
                .eq(StringUtil.isNotEmpty(request.getRiskLevel()), AdminLoginLogDO::getRiskLevel, request.getRiskLevel())
                .like(StringUtil.isNotEmpty(request.getUsername()), AdminLoginLogDO::getUsername, request.getUsername())
                .like(StringUtil.isNotEmpty(request.getLoginLocation()), AdminLoginLogDO::getLoginLocation, request.getLoginLocation())
                .like(StringUtil.isNotEmpty(request.getAbnormalTag()), AdminLoginLogDO::getAbnormalTags, request.getAbnormalTag())
                .ne(Boolean.TRUE.equals(request.getAbnormalOnly()), AdminLoginLogDO::getRiskLevel, RiskLevelEnum.LOW.getValue())
                .between(request.getBeginTime() != null && request.getEndTime() != null,
                        AdminLoginLogDO::getLoginTime,
                        request.getBeginTime(),
                        request.getEndTime())
                .orderByDesc(AdminLoginLogDO::getLogId);
    }

}
