package com.wzkris.principal.service.impl;

import com.wzkris.principal.domain.CustomerInfoDO;
import com.wzkris.principal.domain.CustomerWalletInfoDO;
import com.wzkris.principal.mapper.CustomerInfoMapper;
import com.wzkris.principal.mapper.CustomerWalletInfoMapper;
import com.wzkris.principal.service.CustomerInfoService;
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
