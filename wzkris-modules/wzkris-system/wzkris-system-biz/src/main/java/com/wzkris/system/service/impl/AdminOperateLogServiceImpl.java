package com.wzkris.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wzkris.common.core.utils.StringUtil;
import com.wzkris.system.domain.AdminOperateLogDO;
import com.wzkris.system.mapper.AdminOperateLogMapper;
import com.wzkris.system.request.adminlog.AdminOperateLogQueryRequest;
import com.wzkris.system.service.AdminOperateLogService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 操作日志 服务层处理
 *
 * @author wzkris
 */
@Service
@RequiredArgsConstructor
public class AdminOperateLogServiceImpl
        extends ServiceImpl<AdminOperateLogMapper, AdminOperateLogDO>
        implements AdminOperateLogService {

    @Override
    public List<AdminOperateLogDO> list(AdminOperateLogQueryRequest request) {
        return baseMapper.selectList(this.buildQueryWrapper(request));
    }

    private LambdaQueryWrapper<AdminOperateLogDO> buildQueryWrapper(AdminOperateLogQueryRequest request) {
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

