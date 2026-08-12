package com.wzkris.usercenter.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.wzkris.common.orm.plus.BaseMapperPlus;
import com.wzkris.usercenter.api.tenant.response.TenantInfoQueryResponse;
import com.wzkris.usercenter.api.tenant.response.TenantMngPageResponse;
import com.wzkris.usercenter.api.tenant.response.TenantMngQueryResponse;
import com.wzkris.usercenter.domain.TenantInfoDO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

/**
 * 租户表 数据层
 *
 * @author wzkris
 */
@Mapper
@Repository
public interface TenantInfoMapper extends BaseMapperPlus<TenantInfoDO> {

    @Select("""
            SELECT t.*, p.package_name, p.account_num_limit, p.role_num_limit
            FROM biz.tenant_info t
            LEFT JOIN biz.tenant_package_info p ON t.package_id = p.id
            ${ew.customSqlSegment}
            """)
    IPage<TenantMngPageResponse> selectVOPage(IPage<TenantMngPageResponse> page, @Param(Constants.WRAPPER) Wrapper<TenantInfoDO> wrapper);

    @Select("""
            SELECT t.*, p.package_name, p.account_num_limit, p.role_num_limit
            FROM biz.tenant_info t
            LEFT JOIN biz.tenant_package_info p ON t.package_id = p.id
            WHERE t.deleted = false AND t.id = #{tenantId}
            """)
    TenantMngQueryResponse selectMngVOById(Long tenantId);

    @Select("""
            SELECT t.*, p.package_name
            FROM biz.tenant_info t
            LEFT JOIN biz.tenant_package_info p ON t.package_id = p.id
            WHERE t.deleted = false AND t.id = #{tenantId}
            """)
    TenantInfoQueryResponse selectVOById(Long tenantId);

}

