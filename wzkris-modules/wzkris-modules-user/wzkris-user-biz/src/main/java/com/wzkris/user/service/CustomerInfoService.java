package com.wzkris.user.service;

import com.wzkris.user.domain.CustomerInfoDO;

public interface CustomerInfoService {

    /**
     * 添加客户
     */
    void saveCustomer(CustomerInfoDO customer);

}
