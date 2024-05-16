package com.thingslink.user.mapper;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.thingslink.common.orm.plus.BaseMapperPlus;
import com.thingslink.user.domain.SysTenantPackage;
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
