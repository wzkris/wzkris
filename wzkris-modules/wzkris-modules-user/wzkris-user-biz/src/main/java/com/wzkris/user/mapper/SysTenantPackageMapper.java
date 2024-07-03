package com.wzkris.user.mapper;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.wzkris.common.orm.plus.BaseMapperPlus;
import com.wzkris.user.domain.SysTenantPackage;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SysTenantPackageMapper extends BaseMapperPlus<SysTenantPackage> {

    default List<Long> listMenuIdByPackageId(Long packageId) {
        return this.selectOne(
                new LambdaQueryWrapper<SysTenantPackage>()
                        .select(SysTenantPackage::getMenuIds)
                        .eq(SysTenantPackage::getPackageId, packageId)
        ).getMenuIds();
    }
}
