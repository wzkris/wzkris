package com.wzkris.message.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.wzkris.common.core.utils.StringUtil;
import com.wzkris.message.domain.AdminLoginLogDO;
import com.wzkris.message.domain.req.userlog.AdminLoginLogQueryReq;
import com.wzkris.message.mapper.AdminLoginLogMapper;
import com.wzkris.message.service.AdminLoginLogService;
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
public class AdminLoginLogServiceImpl implements AdminLoginLogService {

    private final AdminLoginLogMapper adminLoginLogMapper;

    @Override
    public List<AdminLoginLogDO> list(AdminLoginLogQueryReq queryReq) {
        return adminLoginLogMapper.selectList(this.buildQueryWrapper(queryReq));
    }

    private LambdaQueryWrapper<AdminLoginLogDO> buildQueryWrapper(AdminLoginLogQueryReq queryReq) {
        return new LambdaQueryWrapper<AdminLoginLogDO>()
                .eq(ObjectUtils.isNotEmpty(queryReq.getAdminId()), AdminLoginLogDO::getAdminId, queryReq.getAdminId())
                .eq(ObjectUtils.isNotEmpty(queryReq.getSuccess()), AdminLoginLogDO::getSuccess, queryReq.getSuccess())
                .like(StringUtil.isNotEmpty(queryReq.getUsername()), AdminLoginLogDO::getUsername, queryReq.getUsername())
                .like(
                        StringUtil.isNotEmpty(queryReq.getLoginLocation()),
                        AdminLoginLogDO::getLoginLocation,
                        queryReq.getLoginLocation())
                .between(
                        queryReq.getParam("beginTime") != null && queryReq.getParam("endTime") != null,
                        AdminLoginLogDO::getLoginTime,
                        queryReq.getParam("beginTime"),
                        queryReq.getParam("endTime"))
                .orderByDesc(AdminLoginLogDO::getLogId);
    }

}
