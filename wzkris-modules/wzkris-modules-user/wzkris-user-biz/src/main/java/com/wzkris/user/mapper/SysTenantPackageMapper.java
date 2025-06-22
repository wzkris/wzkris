package com.wzkris.user.mapper;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.wzkris.common.orm.plus.BaseMapperPlus;
import com.wzkris.user.domain.SysTenantPackage;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Repository;

/**
 * 租户套餐表 数据层
 *
 * @author wzkris
 */
@Repository
public interface SysTenantPackageMapper extends BaseMapperPlus<SysTenantPackage> {

    default List<Long> listMenuIdByPackageId(@Nullable Long packageId) {
        if (packageId == null) {
            return Collections.emptyList();
        }
        SysTenantPackage tenantPackage = this.selectOne(new LambdaQueryWrapper<SysTenantPackage>()
                .select(SysTenantPackage::getMenuIds)
                .eq(SysTenantPackage::getPackageId, packageId));
        return tenantPackage == null ? Collections.emptyList() : Arrays.asList(tenantPackage.getMenuIds());
    }
}
