package com.wzkris.message.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.wzkris.common.core.utils.StringUtil;
import com.wzkris.message.domain.StaffOperateLogDO;
import com.wzkris.message.domain.req.stafflog.StaffOperateLogQueryReq;
import com.wzkris.message.mapper.StaffOperateLogMapper;
import com.wzkris.message.service.StaffOperateLogService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class StaffOperateLogServiceImpl implements StaffOperateLogService {

    private final StaffOperateLogMapper staffOperateLogMapper;

    @Override
    public List<StaffOperateLogDO> list(StaffOperateLogQueryReq queryReq) {
        return staffOperateLogMapper.selectList(this.buildQueryWrapper(queryReq));
    }

    private LambdaQueryWrapper<StaffOperateLogDO> buildQueryWrapper(StaffOperateLogQueryReq queryReq) {
        return new LambdaQueryWrapper<StaffOperateLogDO>()
                .eq(ObjectUtils.isNotEmpty(queryReq.getStaffId()), StaffOperateLogDO::getStaffId, queryReq.getStaffId())
                .eq(ObjectUtils.isNotEmpty(queryReq.getSuccess()), StaffOperateLogDO::getSuccess, queryReq.getSuccess())
                .like(StringUtil.isNotBlank(queryReq.getTitle()), StaffOperateLogDO::getTitle, queryReq.getTitle())
                .like(StringUtil.isNotBlank(queryReq.getSubTitle()), StaffOperateLogDO::getSubTitle, queryReq.getSubTitle())
                .eq(StringUtil.isNotEmpty(queryReq.getOperType()), StaffOperateLogDO::getOperType, queryReq.getOperType())
                .like(StringUtil.isNotBlank(queryReq.getOperName()), StaffOperateLogDO::getOperName, queryReq.getOperName())
                .between(
                        queryReq.getParam("beginTime") != null && queryReq.getParam("endTime") != null,
                        StaffOperateLogDO::getOperTime,
                        queryReq.getParam("beginTime"),
                        queryReq.getParam("endTime"))
                .orderByDesc(StaffOperateLogDO::getOperId);
    }

}
