package com.wzkris.usercenter.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.wzkris.usercenter.domain.TenantPackageInfoDO;
import com.wzkris.usercenter.response.SelectResponse;
import jakarta.annotation.Nullable;

import java.util.List;

/**
 * 租户套餐层
 *
 * @author wzkris
 */
public interface TenantPackageInfoService extends IService<TenantPackageInfoDO> {

    /**
     * 查询可选择套餐
     *
     * @return 套餐列表
     */
    List<SelectResponse> listSelect(@Nullable String packageName);

    /**
     * 校验套餐是否被使用
     *
     * @param packageIds 套餐ID
     * @return 结果
     */
    boolean existInUsed(List<Long> packageIds);

}

