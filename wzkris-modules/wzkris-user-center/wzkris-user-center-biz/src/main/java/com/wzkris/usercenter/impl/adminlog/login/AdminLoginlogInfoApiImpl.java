package com.wzkris.usercenter.impl.adminlog.login;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.wzkris.common.core.model.Result;
import com.wzkris.common.core.utils.BeanCopierUtil;
import com.wzkris.common.core.utils.StringUtil;
import com.wzkris.common.orm.model.Page;
import com.wzkris.common.security.utils.SecurityUtil;
import com.wzkris.common.web.model.AbstractApi;
import com.wzkris.usercenter.api.adminlog.login.AdminLoginlogInfoApi;
import com.wzkris.usercenter.api.adminlog.login.request.AdminLoginLogInfoPageRequest;
import com.wzkris.usercenter.api.adminlog.login.response.AdminLoginLogInfoResponse;
import com.wzkris.usercenter.domain.AdminLoginLogDO;
import com.wzkris.usercenter.service.AdminLoginLogService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AdminLoginlogInfoApiImpl
        extends AbstractApi
        implements AdminLoginlogInfoApi {

    private final AdminLoginLogService adminLoginLogService;

    @Override
    public Result<Page<AdminLoginLogInfoResponse>> queryPage(AdminLoginLogInfoPageRequest request) {
        IPage<AdminLoginLogDO> page = adminLoginLogService.page(request.buildPage(), buildQueryWrapper(request));
        return ok(Page.of(page, BeanCopierUtil.copyList(page.getRecords(), AdminLoginLogInfoResponse.class)));
    }

    private LambdaQueryWrapper<AdminLoginLogDO> buildQueryWrapper(AdminLoginLogInfoPageRequest request) {
        return new LambdaQueryWrapper<AdminLoginLogDO>()
                .eq(AdminLoginLogDO::getAdminId, SecurityUtil.getUid())
                .eq(ObjectUtils.isNotEmpty(request.getSuccess()), AdminLoginLogDO::getSuccess, request.getSuccess())
                .eq(StringUtil.isNotEmpty(request.getTraceId()), AdminLoginLogDO::getTraceId, request.getTraceId())
                .like(StringUtil.isNotEmpty(request.getUsername()), AdminLoginLogDO::getUsername, request.getUsername())
                .like(StringUtil.isNotEmpty(request.getLoginLocation()), AdminLoginLogDO::getLoginLocation, request.getLoginLocation())
                .between(request.getBeginTime() != null && request.getEndTime() != null,
                        AdminLoginLogDO::getLoginTime,
                        request.getBeginTime(),
                        request.getEndTime())
                .orderByDesc(AdminLoginLogDO::getId);
    }

}
