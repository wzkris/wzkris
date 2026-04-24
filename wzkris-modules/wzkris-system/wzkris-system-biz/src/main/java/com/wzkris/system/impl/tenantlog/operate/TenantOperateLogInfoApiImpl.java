package com.wzkris.system.impl.tenantlog.operate;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.wzkris.common.core.model.Result;
import com.wzkris.common.core.utils.StringUtil;
import com.wzkris.common.orm.model.AbstractApi;
import com.wzkris.common.orm.model.Page;
import com.wzkris.common.security.utils.SecurityUtil;
import com.wzkris.common.web.utils.BeanUtil;
import com.wzkris.system.api.tenantlog.operate.TenantOperateLogInfoApi;
import com.wzkris.system.domain.TenantOperateLogDO;
import com.wzkris.system.request.tenantlog.TenantOperateLogInfoPageRequest;
import com.wzkris.system.response.tenantlog.TenantOperateLogInfoResponse;
import com.wzkris.system.service.TenantOperateLogService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TenantOperateLogInfoApiImpl
        extends AbstractApi
        implements TenantOperateLogInfoApi {

    private final TenantOperateLogService tenantOperateLogService;

    @Override
    public Result<Page<TenantOperateLogInfoResponse>> queryPage(TenantOperateLogInfoPageRequest request) {
        startPage();
        List<TenantOperateLogDO> list = tenantOperateLogService.list(buildQueryWrapper(request));
        return getPageResult(BeanUtil.convert(list, TenantOperateLogInfoResponse.class));
    }

    private LambdaQueryWrapper<TenantOperateLogDO> buildQueryWrapper(TenantOperateLogInfoPageRequest request) {
        return new LambdaQueryWrapper<TenantOperateLogDO>()
                .eq(TenantOperateLogDO::getMemberId, SecurityUtil.getUid())
                .eq(ObjectUtils.isNotEmpty(request.getSuccess()), TenantOperateLogDO::getSuccess, request.getSuccess())
                .like(StringUtil.isNotBlank(request.getTitle()), TenantOperateLogDO::getTitle, request.getTitle())
                .like(StringUtil.isNotBlank(request.getSubTitle()), TenantOperateLogDO::getSubTitle, request.getSubTitle())
                .eq(StringUtil.isNotEmpty(request.getOperType()), TenantOperateLogDO::getOperType, request.getOperType())
                .like(StringUtil.isNotBlank(request.getUsername()), TenantOperateLogDO::getUsername, request.getUsername())
                .between(request.getBeginTime() != null && request.getEndTime() != null,
                        TenantOperateLogDO::getOperTime,
                        request.getBeginTime(), request.getEndTime())
                .orderByDesc(TenantOperateLogDO::getOperId);
    }

}
