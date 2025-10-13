package com.wzkris.message.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.wzkris.common.core.utils.StringUtil;
import com.wzkris.message.domain.UserLoginLogDO;
import com.wzkris.message.domain.req.userlog.UserLoginLogQueryReq;
import com.wzkris.message.mapper.UserLoginLogMapper;
import com.wzkris.message.service.UserLoginLogService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author : wzkris
 * @version : V1.0.0
 * @description : 登录日志
 * @date : 2024/1/10 13:55
 */
@Service
@RequiredArgsConstructor
public class UserLoginLogServiceImpl implements UserLoginLogService {

    private final UserLoginLogMapper userLoginLogMapper;

    @Override
    public List<UserLoginLogDO> list(UserLoginLogQueryReq queryReq) {
        return userLoginLogMapper.selectList(this.buildQueryWrapper(queryReq));
    }

    private LambdaQueryWrapper<UserLoginLogDO> buildQueryWrapper(UserLoginLogQueryReq queryReq) {
        return new LambdaQueryWrapper<UserLoginLogDO>()
                .eq(ObjectUtils.isNotEmpty(queryReq.getUserId()), UserLoginLogDO::getUserId, queryReq.getUserId())
                .eq(StringUtil.isNotEmpty(queryReq.getStatus()), UserLoginLogDO::getStatus, queryReq.getStatus())
                .like(StringUtil.isNotEmpty(queryReq.getUsername()), UserLoginLogDO::getUsername, queryReq.getUsername())
                .like(
                        StringUtil.isNotEmpty(queryReq.getLoginLocation()),
                        UserLoginLogDO::getLoginLocation,
                        queryReq.getLoginLocation())
                .between(
                        queryReq.getParam("beginTime") != null && queryReq.getParam("endTime") != null,
                        UserLoginLogDO::getLoginTime,
                        queryReq.getParam("beginTime"),
                        queryReq.getParam("endTime"))
                .orderByDesc(UserLoginLogDO::getLogId);
    }

}
