package com.wzkris.usercenter.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.wzkris.usercenter.domain.CustomerInfoDO;
import com.wzkris.usercenter.domain.CustomerSocialInfoDO;

public interface CustomerInfoService extends IService<CustomerInfoDO> {

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
