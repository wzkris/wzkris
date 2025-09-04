package com.wzkris.user.service;

import com.wzkris.user.domain.vo.SelectVO;
import jakarta.annotation.Nullable;

import java.util.List;

/**
 * 租户套餐层
 *
 * @author wzkris
 */
public interface TenantPackageInfoService {

    /**
     * 查询可选择套餐
     *
     * @return 套餐列表
     */
    List<SelectVO> listSelect(@Nullable String packageName);

    /**
     * 校验套餐是否被使用
     *
     * @param packageIds 套餐ID
     * @return 结果
     */
    boolean checkPackageUsed(List<Long> packageIds);

}
