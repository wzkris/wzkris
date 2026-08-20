package com.wzkris.usercenter.service.impl;

import com.wzkris.common.orm.plus.ServiceImplPlus;
import com.wzkris.usercenter.domain.CustomerLoginLogDO;
import com.wzkris.usercenter.mapper.CustomerLoginLogMapper;
import com.wzkris.usercenter.service.CustomerLoginLogService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * 用户登录日志 服务层处理
 *
 * @author wzkris
 */
@Service
@RequiredArgsConstructor
public class CustomerLoginLogServiceImpl
        extends ServiceImplPlus<CustomerLoginLogMapper, CustomerLoginLogDO>
        implements CustomerLoginLogService {

}
