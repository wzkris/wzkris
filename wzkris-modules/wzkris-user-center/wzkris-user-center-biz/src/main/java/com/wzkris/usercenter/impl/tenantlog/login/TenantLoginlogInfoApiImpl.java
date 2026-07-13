package com.wzkris.usercenter.impl.tenantlog.login;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.wzkris.common.core.model.Result;
import com.wzkris.common.core.utils.StringUtil;
import com.wzkris.common.orm.model.AbstractApi;
import com.wzkris.common.orm.model.Page;
import com.wzkris.common.security.utils.SecurityUtil;
import com.wzkris.common.web.utils.BeanUtil;
import com.wzkris.usercenter.api.tenantlog.login.TenantLoginlogInfoApi;
import com.wzkris.usercenter.api.tenantlog.login.request.TenantLoginLogInfoPageRequest;
import com.wzkris.usercenter.api.tenantlog.login.response.TenantLoginLogInfoResponse;
import com.wzkris.usercenter.domain.TenantLoginLogDO;
import com.wzkris.usercenter.service.TenantLoginLogService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TenantLoginlogInfoApiImpl
        extends AbstractApi
        implements TenantLoginlogInfoApi {

    private final TenantLoginLogService tenantLoginLogService;

    @Override
    public Result<Page<TenantLoginLogInfoResponse>> queryPage(TenantLoginLogInfoPageRequest request) {
        startPage(request);
        List<TenantLoginLogDO> list = tenantLoginLogService.list(buildQueryWrapper(request));
        return getPageResult(BeanUtil.convert(list, TenantLoginLogInfoResponse.class));
    }

    private LambdaQueryWrapper<TenantLoginLogDO> buildQueryWrapper(TenantLoginLogInfoPageRequest request) {
        return new LambdaQueryWrapper<TenantLoginLogDO>()
                .eq(TenantLoginLogDO::getMemberId, SecurityUtil.getUid())
                .eq(ObjectUtils.isNotEmpty(request.getSuccess()), TenantLoginLogDO::getSuccess, request.getSuccess())
                .eq(StringUtil.isNotEmpty(request.getTraceId()), TenantLoginLogDO::getTraceId, request.getTraceId())
                .like(StringUtil.isNotEmpty(request.getUsername()), TenantLoginLogDO::getUsername, request.getUsername())
                .like(StringUtil.isNotEmpty(request.getLoginLocation()), TenantLoginLogDO::getLoginLocation, request.getLoginLocation())
                .between(request.getBeginTime() != null && request.getEndTime() != null,
                        TenantLoginLogDO::getLoginTime,
                        request.getBeginTime(),
                        request.getEndTime())
                .orderByDesc(TenantLoginLogDO::getLogId);
    }

}
