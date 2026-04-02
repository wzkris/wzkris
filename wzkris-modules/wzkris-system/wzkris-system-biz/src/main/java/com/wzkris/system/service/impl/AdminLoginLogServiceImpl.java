package com.wzkris.system.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wzkris.system.domain.AdminLoginLogDO;
import com.wzkris.system.mapper.AdminLoginLogMapper;
import com.wzkris.system.service.AdminLoginLogService;
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
        extends ServiceImpl<AdminLoginLogMapper, AdminLoginLogDO>
        implements AdminLoginLogService {

}

