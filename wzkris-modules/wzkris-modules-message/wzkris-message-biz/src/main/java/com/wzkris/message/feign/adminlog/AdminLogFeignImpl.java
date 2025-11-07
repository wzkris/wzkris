package com.wzkris.message.feign.adminlog;

import com.wzkris.common.web.utils.BeanUtil;
import com.wzkris.message.domain.AdminLoginLogDO;
import com.wzkris.message.domain.AdminOperateLogDO;
import com.wzkris.message.feign.adminlog.req.LoginLogReq;
import com.wzkris.message.feign.adminlog.req.OperateLogReq;
import com.wzkris.message.mapper.AdminLoginLogMapper;
import com.wzkris.message.mapper.AdminOperateLogMapper;
import io.swagger.v3.oas.annotations.Hidden;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Hidden
@RestController
@RequestMapping("/feign-admin-log")
@RequiredArgsConstructor
public class AdminLogFeignImpl implements AdminLogFeign {

    private final AdminOperateLogMapper adminOperateLogMapper;

    private final AdminLoginLogMapper adminLoginLogMapper;

    @Override
    public void saveOperlogs(@RequestBody List<OperateLogReq> operateLogReqs) {
        List<AdminOperateLogDO> operLogs = BeanUtil.convert(operateLogReqs, AdminOperateLogDO.class);
        adminOperateLogMapper.insert(operLogs, 1000);
    }

    @Override
    public void saveLoginlog(@RequestBody LoginLogReq loginLogReq) {
        AdminLoginLogDO adminLoginLogDO = BeanUtil.convert(loginLogReq, AdminLoginLogDO.class);
        adminLoginLogMapper.insert(adminLoginLogDO);
    }

}
