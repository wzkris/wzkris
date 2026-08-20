package com.wzkris.usercenter.impl.customerlog.login;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.wzkris.common.core.model.Result;
import com.wzkris.common.core.utils.BeanCopierUtil;
import com.wzkris.common.core.utils.StringUtil;
import com.wzkris.common.orm.model.Page;
import com.wzkris.common.security.utils.SecurityUtil;
import com.wzkris.common.web.model.AbstractApi;
import com.wzkris.usercenter.api.customerlog.login.CustomerLoginlogInfoApi;
import com.wzkris.usercenter.api.customerlog.login.request.CustomerLoginLogInfoPageRequest;
import com.wzkris.usercenter.api.customerlog.login.response.CustomerLoginLogInfoPageResponse;
import com.wzkris.usercenter.domain.CustomerLoginLogDO;
import com.wzkris.usercenter.service.CustomerLoginLogService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomerLoginlogInfoApiImpl
        extends AbstractApi
        implements CustomerLoginlogInfoApi {

    private final CustomerLoginLogService customerLoginLogService;

    @Override
    public Result<Page<CustomerLoginLogInfoPageResponse>> queryPage(CustomerLoginLogInfoPageRequest request) {
        IPage<CustomerLoginLogDO> page = customerLoginLogService.page(request.buildPage(), buildQueryWrapper(request));
        return ok(Page.of(page, BeanCopierUtil.copyList(page.getRecords(), CustomerLoginLogInfoPageResponse.class)));
    }

    private LambdaQueryWrapper<CustomerLoginLogDO> buildQueryWrapper(CustomerLoginLogInfoPageRequest request) {
        return new LambdaQueryWrapper<CustomerLoginLogDO>()
                .eq(CustomerLoginLogDO::getCustomerId, SecurityUtil.getUid())
                .eq(ObjectUtils.isNotEmpty(request.getSuccess()), CustomerLoginLogDO::getSuccess, request.getSuccess())
                .eq(StringUtil.isNotEmpty(request.getTraceId()), CustomerLoginLogDO::getTraceId, request.getTraceId())
                .like(StringUtil.isNotEmpty(request.getUsername()), CustomerLoginLogDO::getUsername, request.getUsername())
                .like(StringUtil.isNotEmpty(request.getLoginLocation()), CustomerLoginLogDO::getLoginLocation, request.getLoginLocation())
                .between(request.getBeginTime() != null && request.getEndTime() != null,
                        CustomerLoginLogDO::getLoginTime,
                        request.getBeginTime(),
                        request.getEndTime())
                .orderByDesc(CustomerLoginLogDO::getId);
    }

}
