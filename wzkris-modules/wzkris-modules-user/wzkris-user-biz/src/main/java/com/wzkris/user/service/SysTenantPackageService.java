package com.wzkris.user.service;


import java.util.List;

/**
 * 租户套餐层
 *
 * @author wzkris
 */
public interface SysTenantPackageService {

    /**
     * 校验套餐是否被使用
     *
     * @param packageIds 套餐ID
     * @return 结果
     */
    boolean checkPackageUsed(List<Long> packageIds);
}
