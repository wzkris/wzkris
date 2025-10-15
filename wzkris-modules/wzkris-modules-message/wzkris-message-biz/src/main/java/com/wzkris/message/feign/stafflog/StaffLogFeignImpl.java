package com.wzkris.message.feign.stafflog;

import com.wzkris.common.web.utils.BeanUtil;
import com.wzkris.message.domain.StaffLoginLogDO;
import com.wzkris.message.domain.StaffOperateLogDO;
import com.wzkris.message.feign.stafflog.req.StaffLoginLogReq;
import com.wzkris.message.feign.stafflog.req.StaffOperateLogReq;
import com.wzkris.message.mapper.StaffLoginLogMapper;
import com.wzkris.message.mapper.StaffOperateLogMapper;
import io.swagger.v3.oas.annotations.Hidden;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Hidden
@RestController
@RequestMapping("/feign-staff-log")
@RequiredArgsConstructor
public class StaffLogFeignImpl implements StaffLogFeign {

    private final StaffOperateLogMapper operateLogMapper;

    private final StaffLoginLogMapper loginLogMapper;

    @Override
    public void saveOperlogs(@RequestBody List<StaffOperateLogReq> staffOperateLogReqs) {
        List<StaffOperateLogDO> operLogs = BeanUtil.convert(staffOperateLogReqs, StaffOperateLogDO.class);
        operateLogMapper.insert(operLogs, 1000);
    }

    @Override
    public void saveLoginlog(@RequestBody StaffLoginLogReq staffLoginLogReq) {
        StaffLoginLogDO loginLogDO = BeanUtil.convert(staffLoginLogReq, StaffLoginLogDO.class);
        loginLogMapper.insert(loginLogDO);
    }

}
