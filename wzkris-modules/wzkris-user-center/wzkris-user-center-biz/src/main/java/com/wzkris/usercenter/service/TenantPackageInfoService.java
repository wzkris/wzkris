package com.wzkris.usercenter.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.wzkris.usercenter.domain.TenantPackageInfoDO;

import java.util.List;

/**
 * 租户套餐层
 *
 * @author wzkris
 */
public interface TenantPackageInfoService extends IService<TenantPackageInfoDO> {

    /**
     * 校验套餐是否被使用
     *
     * @param packageIds 套餐ID
     * @return 结果
     */
    boolean existInUsed(List<Long> packageIds);

}

