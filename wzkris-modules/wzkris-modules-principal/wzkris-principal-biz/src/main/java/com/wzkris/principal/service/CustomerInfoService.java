package com.wzkris.principal.service;

import com.wzkris.principal.domain.CustomerInfoDO;
import com.wzkris.principal.domain.CustomerSocialInfoDO;

public interface CustomerInfoService {

    /**
     * 添加顾客
     */
    void saveCustomer(CustomerInfoDO customer);

    /**
     * 第三方注册
     *
     * @param customer   顾客信息
     * @param socialInfo 三方信息
     * @return 顾客ID
     */
    Long registerBySocial(CustomerInfoDO customer, CustomerSocialInfoDO socialInfo);

}
