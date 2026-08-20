package com.wzkris.usercenter.service.impl;

import com.wzkris.common.orm.plus.ServiceImplPlus;
import com.wzkris.usercenter.domain.CustomerOperateLogDO;
import com.wzkris.usercenter.mapper.CustomerOperateLogMapper;
import com.wzkris.usercenter.service.CustomerOperateLogService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * 用户操作日志 服务层处理
 *
 * @author wzkris
 */
@Service
@RequiredArgsConstructor
public class CustomerOperateLogServiceImpl
        extends ServiceImplPlus<CustomerOperateLogMapper, CustomerOperateLogDO>
        implements CustomerOperateLogService {

}
