package com.wzkris.user.mapper;

import com.wzkris.common.orm.plus.BaseMapperPlus;
import com.wzkris.user.domain.SysTenant;
import com.wzkris.user.domain.vo.SysTenantVO;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

/**
 * 租户表 数据层
 *
 * @author wzkris
 */
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

    @Select("""
             SELECT u.username AS administrator, t.*
             FROM sys_tenant t
             LEFT JOIN sys_user u ON
             t.administrator = u.user_id
             WHERE t.tenant_id = #{tenantId}
            """)
    SysTenantVO selectVOById(Long tenantId);
}
