package com.wzkris.usercenter.impl.customerlog.operate;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.wzkris.common.core.model.Result;
import com.wzkris.common.core.utils.BeanCopierUtil;
import com.wzkris.common.core.utils.StringUtil;
import com.wzkris.common.orm.model.Page;
import com.wzkris.common.security.utils.SecurityUtil;
import com.wzkris.common.web.model.AbstractApi;
import com.wzkris.usercenter.api.customerlog.operate.CustomerOperateLogInfoApi;
import com.wzkris.usercenter.api.customerlog.operate.request.CustomerOperateLogInfoPageRequest;
import com.wzkris.usercenter.api.customerlog.operate.response.CustomerOperateLogInfoResponse;
import com.wzkris.usercenter.domain.CustomerOperateLogDO;
import com.wzkris.usercenter.service.CustomerOperateLogService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomerOperateLogInfoApiImpl
        extends AbstractApi
        implements CustomerOperateLogInfoApi {

    private final CustomerOperateLogService customerOperateLogService;

    @Override
    public Result<Page<CustomerOperateLogInfoResponse>> queryPage(CustomerOperateLogInfoPageRequest request) {
        IPage<CustomerOperateLogDO> page = customerOperateLogService.page(request.buildPage(), buildQueryWrapper(request));
        return ok(Page.of(page, BeanCopierUtil.copyList(page.getRecords(), CustomerOperateLogInfoResponse.class)));
    }

    private LambdaQueryWrapper<CustomerOperateLogDO> buildQueryWrapper(CustomerOperateLogInfoPageRequest request) {
        return new LambdaQueryWrapper<CustomerOperateLogDO>()
                .eq(CustomerOperateLogDO::getCustomerId, SecurityUtil.getUid())
                .eq(ObjectUtils.isNotEmpty(request.getSuccess()), CustomerOperateLogDO::getSuccess, request.getSuccess())
                .eq(StringUtil.isNotEmpty(request.getTraceId()), CustomerOperateLogDO::getTraceId, request.getTraceId())
                .like(StringUtil.isNotBlank(request.getTitle()), CustomerOperateLogDO::getTitle, request.getTitle())
                .like(StringUtil.isNotBlank(request.getSubTitle()), CustomerOperateLogDO::getSubTitle, request.getSubTitle())
                .eq(StringUtil.isNotEmpty(request.getOperType()), CustomerOperateLogDO::getOperType, request.getOperType())
                .like(StringUtil.isNotBlank(request.getOperName()), CustomerOperateLogDO::getUsername, request.getOperName())
                .between(request.getBeginTime() != null && request.getEndTime() != null,
                        CustomerOperateLogDO::getOperTime,
                        request.getBeginTime(), request.getEndTime())
                .orderByDesc(CustomerOperateLogDO::getOperId);
    }

}
