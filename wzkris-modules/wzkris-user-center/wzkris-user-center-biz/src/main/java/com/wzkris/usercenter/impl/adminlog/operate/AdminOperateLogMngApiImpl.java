package com.wzkris.usercenter.impl.adminlog.operate;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.wzkris.common.core.model.Result;
import com.wzkris.common.core.utils.BeanCopierUtil;
import com.wzkris.common.core.utils.StringUtil;
import com.wzkris.common.orm.model.Page;
import com.wzkris.common.web.model.AbstractApi;
import com.wzkris.usercenter.api.adminlog.operate.AdminOperateLogMngApi;
import com.wzkris.usercenter.api.adminlog.operate.request.AdminOperateLogMngPageRequest;
import com.wzkris.usercenter.api.adminlog.operate.response.AdminOperateLogMngResponse;
import com.wzkris.usercenter.domain.AdminOperateLogDO;
import com.wzkris.usercenter.service.AdminOperateLogService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AdminOperateLogMngApiImpl
        extends AbstractApi
        implements AdminOperateLogMngApi {

    private final AdminOperateLogService adminOperateLogService;

    @Override
    public Result<Page<AdminOperateLogMngResponse>> queryPage(AdminOperateLogMngPageRequest request) {
        IPage<AdminOperateLogDO> page = adminOperateLogService.page(request.buildPage(), buildQueryWrapper(request));
        return ok(Page.of(page, BeanCopierUtil.copyList(page.getRecords(), AdminOperateLogMngResponse.class)));
    }

    private LambdaQueryWrapper<AdminOperateLogDO> buildQueryWrapper(AdminOperateLogMngPageRequest request) {
        return new LambdaQueryWrapper<AdminOperateLogDO>()
                .eq(ObjectUtils.isNotEmpty(request.getAdminId()), AdminOperateLogDO::getAdminId, request.getAdminId())
                .eq(ObjectUtils.isNotEmpty(request.getSuccess()), AdminOperateLogDO::getSuccess, request.getSuccess())
                .eq(StringUtil.isNotEmpty(request.getTraceId()), AdminOperateLogDO::getTraceId, request.getTraceId())
                .like(StringUtil.isNotBlank(request.getTitle()), AdminOperateLogDO::getTitle, request.getTitle())
                .like(StringUtil.isNotBlank(request.getSubTitle()), AdminOperateLogDO::getSubTitle, request.getSubTitle())
                .eq(StringUtil.isNotEmpty(request.getOperType()), AdminOperateLogDO::getOperType, request.getOperType())
                .like(StringUtil.isNotBlank(request.getOperName()), AdminOperateLogDO::getUsername, request.getOperName())
                .between(request.getBeginTime() != null && request.getEndTime() != null,
                        AdminOperateLogDO::getOperTime,
                        request.getBeginTime(), request.getEndTime())
                .orderByDesc(AdminOperateLogDO::getOperId);
    }

}
