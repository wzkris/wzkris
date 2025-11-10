package com.wzkris.message.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.wzkris.common.core.utils.StringUtil;
import com.wzkris.message.domain.AdminOperateLogDO;
import com.wzkris.message.domain.req.adminlog.AdminOperateLogQueryReq;
import com.wzkris.message.mapper.AdminOperateLogMapper;
import com.wzkris.message.service.AdminOperateLogService;
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
public class AdminOperateLogServiceImpl implements AdminOperateLogService {

    private final AdminOperateLogMapper adminOperateLogMapper;

    @Override
    public List<AdminOperateLogDO> list(AdminOperateLogQueryReq queryReq) {
        return adminOperateLogMapper.selectList(this.buildQueryWrapper(queryReq));
    }

    private LambdaQueryWrapper<AdminOperateLogDO> buildQueryWrapper(AdminOperateLogQueryReq queryReq) {
        return new LambdaQueryWrapper<AdminOperateLogDO>()
                .eq(ObjectUtils.isNotEmpty(queryReq.getAdminId()), AdminOperateLogDO::getAdminId, queryReq.getAdminId())
                .eq(ObjectUtils.isNotEmpty(queryReq.getSuccess()), AdminOperateLogDO::getSuccess, queryReq.getSuccess())
                .like(StringUtil.isNotBlank(queryReq.getTitle()), AdminOperateLogDO::getTitle, queryReq.getTitle())
                .like(StringUtil.isNotBlank(queryReq.getSubTitle()), AdminOperateLogDO::getSubTitle, queryReq.getSubTitle())
                .eq(StringUtil.isNotEmpty(queryReq.getOperType()), AdminOperateLogDO::getOperType, queryReq.getOperType())
                .like(StringUtil.isNotBlank(queryReq.getOperName()), AdminOperateLogDO::getUsername, queryReq.getOperName())
                .between(
                        queryReq.getParam("beginTime") != null && queryReq.getParam("endTime") != null,
                        AdminOperateLogDO::getOperTime,
                        queryReq.getParam("beginTime"),
                        queryReq.getParam("endTime"))
                .orderByDesc(AdminOperateLogDO::getOperId);
    }

}
