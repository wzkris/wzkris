package com.wzkris.user.mapper;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.wzkris.common.orm.plus.BaseMapperPlus;
import com.wzkris.user.domain.TenantPackageInfoDO;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Repository;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * 租户套餐表 数据层
 *
 * @author wzkris
 */
@Repository
public interface TenantPackageInfoMapper extends BaseMapperPlus<TenantPackageInfoDO> {

    default List<Long> listMenuIdByPackageId(@Nullable Long packageId) {
        if (packageId == null) {
            return Collections.emptyList();
        }
        TenantPackageInfoDO tenantPackage = this.selectOne(new LambdaQueryWrapper<TenantPackageInfoDO>()
                .select(TenantPackageInfoDO::getMenuIds)
                .eq(TenantPackageInfoDO::getPackageId, packageId));
        return tenantPackage == null ? Collections.emptyList() : Arrays.asList(tenantPackage.getMenuIds());
    }

}
