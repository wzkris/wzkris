package com.wzkris.message.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.wzkris.common.core.utils.StringUtil;
import com.wzkris.message.domain.StaffLoginLogDO;
import com.wzkris.message.domain.req.stafflog.StaffLoginLogQueryReq;
import com.wzkris.message.mapper.StaffLoginLogMapper;
import com.wzkris.message.service.StaffLoginLogService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class StaffLoginLogServiceImpl implements StaffLoginLogService {

    private final StaffLoginLogMapper staffLoginLogMapper;

    @Override
    public List<StaffLoginLogDO> list(StaffLoginLogQueryReq queryReq) {
        return staffLoginLogMapper.selectList(this.buildQueryWrapper(queryReq));
    }

    private LambdaQueryWrapper<StaffLoginLogDO> buildQueryWrapper(StaffLoginLogQueryReq queryReq) {
        return new LambdaQueryWrapper<StaffLoginLogDO>()
                .eq(ObjectUtils.isNotEmpty(queryReq.getStaffId()), StaffLoginLogDO::getStaffId, queryReq.getStaffId())
                .eq(ObjectUtils.isNotEmpty(queryReq.getSuccess()), StaffLoginLogDO::getSuccess, queryReq.getSuccess())
                .like(StringUtil.isNotEmpty(queryReq.getStaffName()), StaffLoginLogDO::getStaffName, queryReq.getStaffName())
                .like(
                        StringUtil.isNotEmpty(queryReq.getLoginLocation()),
                        StaffLoginLogDO::getLoginLocation,
                        queryReq.getLoginLocation())
                .between(
                        queryReq.getParam("beginTime") != null && queryReq.getParam("endTime") != null,
                        StaffLoginLogDO::getLoginTime,
                        queryReq.getParam("beginTime"),
                        queryReq.getParam("endTime"))
                .orderByDesc(StaffLoginLogDO::getLogId);
    }

}
