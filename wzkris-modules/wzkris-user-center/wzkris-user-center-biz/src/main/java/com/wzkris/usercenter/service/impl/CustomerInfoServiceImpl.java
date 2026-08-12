package com.wzkris.usercenter.service.impl;

import com.wzkris.common.orm.plus.ServiceImplPlus;
import com.wzkris.usercenter.domain.CustomerInfoDO;
import com.wzkris.usercenter.domain.CustomerSocialInfoDO;
import com.wzkris.usercenter.mapper.CustomerInfoMapper;
import com.wzkris.usercenter.mapper.CustomerSocialInfoMapper;
import com.wzkris.usercenter.service.CustomerInfoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class CustomerInfoServiceImpl
        extends ServiceImplPlus<CustomerInfoMapper, CustomerInfoDO>
        implements CustomerInfoService {

    private final CustomerSocialInfoMapper customerSocialInfoMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveCustomer(CustomerInfoDO customer) {
        baseMapper.insert(customer);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long registerBySocial(CustomerInfoDO customer, CustomerSocialInfoDO socialInfo) {
        this.saveCustomer(customer);

        socialInfo.setCustomerId(customer.getId());
        customerSocialInfoMapper.insert(socialInfo);

        return customer.getId();
    }

}
