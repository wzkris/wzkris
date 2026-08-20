package com.wzkris.usercenter.service.impl;

import com.wzkris.common.orm.plus.ServiceImplPlus;
import com.wzkris.usercenter.domain.AdminLoginLogDO;
import com.wzkris.usercenter.mapper.AdminLoginLogMapper;
import com.wzkris.usercenter.service.AdminLoginLogService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * @author : wzkris
 * @version : V1.0.0
 * @description : 登录日志
 * @date : 2024/1/10 13:55
 */
@Service
@RequiredArgsConstructor
public class AdminLoginLogServiceImpl
        extends ServiceImplPlus<AdminLoginLogMapper, AdminLoginLogDO>
        implements AdminLoginLogService {

}
