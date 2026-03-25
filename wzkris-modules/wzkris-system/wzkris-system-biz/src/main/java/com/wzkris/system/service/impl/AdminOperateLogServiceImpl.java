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
    public List<AdminOperateLogDO> list(AdminOperateLogQueryRequest QueryRequest) {
        return baseMapper.selectList(this.buildQueryWrapper(QueryRequest));
    }

    private LambdaQueryWrapper<AdminOperateLogDO> buildQueryWrapper(AdminOperateLogQueryRequest QueryRequest) {
        return new LambdaQueryWrapper<AdminOperateLogDO>()
                .eq(ObjectUtils.isNotEmpty(QueryRequest.getAdminId()), AdminOperateLogDO::getAdminId, QueryRequest.getAdminId())
                .eq(ObjectUtils.isNotEmpty(QueryRequest.getSuccess()), AdminOperateLogDO::getSuccess, QueryRequest.getSuccess())
                .like(StringUtil.isNotBlank(QueryRequest.getTitle()), AdminOperateLogDO::getTitle, QueryRequest.getTitle())
                .like(StringUtil.isNotBlank(QueryRequest.getSubTitle()), AdminOperateLogDO::getSubTitle, QueryRequest.getSubTitle())
                .eq(StringUtil.isNotEmpty(QueryRequest.getOperType()), AdminOperateLogDO::getOperType, QueryRequest.getOperType())
                .like(StringUtil.isNotBlank(QueryRequest.getOperName()), AdminOperateLogDO::getUsername, QueryRequest.getOperName())
                .between(
                        QueryRequest.getParam("beginTime") != null && QueryRequest.getParam("endTime") != null,
                        AdminOperateLogDO::getOperTime,
                        QueryRequest.getParam("beginTime"),
                        QueryRequest.getParam("endTime"))
                .orderByDesc(AdminOperateLogDO::getOperId);
    }

}

