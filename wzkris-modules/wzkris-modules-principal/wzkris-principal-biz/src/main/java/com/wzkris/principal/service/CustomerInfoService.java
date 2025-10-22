package com.wzkris.principal.service;

import com.wzkris.principal.domain.CustomerInfoDO;

public interface CustomerInfoService {

    /**
     * 添加客户
     */
    void saveCustomer(CustomerInfoDO customer);

}
