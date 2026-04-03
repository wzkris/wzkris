package com.wzkris.system.impl.adminlog.operate;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.wzkris.common.core.model.Result;
import com.wzkris.common.core.utils.StringUtil;
import com.wzkris.common.orm.model.AbstractApi;
import com.wzkris.common.orm.model.Page;
import com.wzkris.common.web.utils.BeanUtil;
import com.wzkris.system.api.adminlog.operate.AdminOperateLogMngApi;
import com.wzkris.system.domain.AdminOperateLogDO;
import com.wzkris.system.request.adminlog.AdminOperateLogMngQueryRequest;
import com.wzkris.system.response.adminlog.AdminOperateLogMngResponse;
import com.wzkris.system.service.AdminOperateLogService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminOperateLogMngApiImpl
        extends AbstractApi
        implements AdminOperateLogMngApi {

    private final AdminOperateLogService adminOperateLogService;

    @Override
    public Result<Page<AdminOperateLogMngResponse>> queryPage(AdminOperateLogMngQueryRequest request) {
        startPage();
        List<AdminOperateLogDO> list = adminOperateLogService.list(buildQueryWrapper(request));
        return getPageResult(BeanUtil.convert(list, AdminOperateLogMngResponse.class));
    }

    private LambdaQueryWrapper<AdminOperateLogDO> buildQueryWrapper(AdminOperateLogMngQueryRequest request) {
        return new LambdaQueryWrapper<AdminOperateLogDO>()
                .eq(ObjectUtils.isNotEmpty(request.getAdminId()), AdminOperateLogDO::getAdminId, request.getAdminId())
                .eq(ObjectUtils.isNotEmpty(request.getSuccess()), AdminOperateLogDO::getSuccess, request.getSuccess())
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
