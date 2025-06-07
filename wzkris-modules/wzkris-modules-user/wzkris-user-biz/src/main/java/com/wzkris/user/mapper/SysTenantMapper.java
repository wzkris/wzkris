package com.wzkris.user.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.wzkris.common.orm.plus.BaseMapperPlus;
import com.wzkris.user.domain.SysTenant;
import com.wzkris.user.domain.vo.SysTenantProfileVO;
import com.wzkris.user.domain.vo.SysTenantVO;
import java.util.List;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

/**
 * 租户表 数据层
 *
 * @author wzkris
 */
@Repository
public interface SysTenantMapper extends BaseMapperPlus<SysTenant> {

    @Select("SELECT oper_pwd FROM biz.sys_tenant WHERE tenant_id = #{tenantId}")
    String selectOperPwdById(Long tenantId);

    @Select(
            """
            SELECT t.*, p.package_name, w.balance FROM biz.sys_tenant t LEFT JOIN biz.sys_tenant_package p ON t.package_id = p.package_id
            LEFT JOIN biz.sys_tenant_wallet w ON t.tenant_id = w.tenant_id
            ${ew.customSqlSegment}
            """)
    List<SysTenantVO> selectVOList(@Param(Constants.WRAPPER) Wrapper<SysTenant> wrapper);

    /**
     * 根据用户ID查询套餐ID，如果查到则说明是租户最高管理员
     *
     * @param userId 用户ID
     * @return 套餐ID
     */
    @Select("SELECT package_id FROM biz.sys_tenant WHERE administrator = #{userId}")
    Long selectPackageIdByUserId(Long userId);

    @Select(
            """
             SELECT t.*, p.package_name FROM biz.sys_tenant t LEFT JOIN biz.sys_tenant_package p ON t.package_id = p.package_id
                        WHERE t.tenant_id = #{tenantId}
            """)
    SysTenantProfileVO selectVOById(Long tenantId);
}
