package com.thingslink.user.mapper;

import com.thingslink.user.domain.SysTenant;
import com.thingslink.common.orm.plus.BaseMapperPlus;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

@Repository
public interface SysTenantMapper extends BaseMapperPlus<SysTenant> {

    /**
     * 根据用户ID查询套餐ID，如果查到则说明是租户最高管理员
     *
     * @param userId 用户ID
     * @return 套餐ID
     */
    @Select("SELECT package_id FROM sys_tenant WHERE administrator = #{userId}")
    Long selectPackageIdByUserId(Long userId);
}