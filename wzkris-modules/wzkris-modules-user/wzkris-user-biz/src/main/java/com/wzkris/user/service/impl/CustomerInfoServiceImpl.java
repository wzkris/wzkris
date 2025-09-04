package com.wzkris.user.service.impl;

import com.wzkris.user.domain.CustomerInfoDO;
import com.wzkris.user.domain.CustomerWalletInfoDO;
import com.wzkris.user.mapper.CustomerInfoMapper;
import com.wzkris.user.mapper.CustomerWalletInfoMapper;
import com.wzkris.user.service.CustomerInfoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class CustomerInfoServiceImpl implements CustomerInfoService {

    private final CustomerInfoMapper customerInfoMapper;

    private final CustomerWalletInfoMapper customerWalletInfoMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveCustomer(CustomerInfoDO customer) {
        customerInfoMapper.insert(customer);
        CustomerWalletInfoDO wallet = new CustomerWalletInfoDO(customer.getCustomerId());
        customerWalletInfoMapper.insert(wallet);
    }

}
