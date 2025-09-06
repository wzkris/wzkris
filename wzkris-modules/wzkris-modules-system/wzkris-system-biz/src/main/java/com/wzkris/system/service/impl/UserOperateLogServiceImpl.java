package com.wzkris.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.wzkris.common.core.utils.StringUtil;
import com.wzkris.system.domain.UserOperateLogDO;
import com.wzkris.system.domain.req.userlog.UserOperateLogQueryReq;
import com.wzkris.system.mapper.UserOperateLogMapper;
import com.wzkris.system.service.UserOperateLogService;
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
public class UserOperateLogServiceImpl implements UserOperateLogService {

    private final UserOperateLogMapper userOperateLogMapper;

    @Override
    public List<UserOperateLogDO> list(UserOperateLogQueryReq queryReq) {
        return userOperateLogMapper.selectList(this.buildQueryWrapper(queryReq));
    }

    private LambdaQueryWrapper<UserOperateLogDO> buildQueryWrapper(UserOperateLogQueryReq queryReq) {
        return new LambdaQueryWrapper<UserOperateLogDO>()
                .eq(ObjectUtils.isNotEmpty(queryReq.getUserId()), UserOperateLogDO::getUserId, queryReq.getUserId())
                .eq(StringUtil.isNotBlank(queryReq.getStatus()), UserOperateLogDO::getStatus, queryReq.getStatus())
                .like(StringUtil.isNotBlank(queryReq.getTitle()), UserOperateLogDO::getTitle, queryReq.getTitle())
                .like(StringUtil.isNotBlank(queryReq.getSubTitle()), UserOperateLogDO::getSubTitle, queryReq.getSubTitle())
                .eq(StringUtil.isNotEmpty(queryReq.getOperType()), UserOperateLogDO::getOperType, queryReq.getOperType())
                .like(StringUtil.isNotBlank(queryReq.getOperName()), UserOperateLogDO::getOperName, queryReq.getOperName())
                .between(
                        queryReq.getParam("beginTime") != null && queryReq.getParam("endTime") != null,
                        UserOperateLogDO::getOperTime,
                        queryReq.getParam("beginTime"),
                        queryReq.getParam("endTime"))
                .orderByDesc(UserOperateLogDO::getOperId);
    }

}
