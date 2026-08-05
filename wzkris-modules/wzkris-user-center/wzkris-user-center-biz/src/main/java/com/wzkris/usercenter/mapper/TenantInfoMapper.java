package com.wzkris.usercenter.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.wzkris.common.orm.plus.BaseMapperPlus;
import com.wzkris.usercenter.api.tenant.response.TenantInfoResponse;
import com.wzkris.usercenter.api.tenant.response.TenantMngResponse;
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
            SELECT t.*, p.package_name, p.member_num_limit, p.post_num_limit, w.balance FROM biz.tenant_info t LEFT JOIN biz.tenant_package_info p ON t.package_id = p.id
            LEFT JOIN biz.tenant_wallet_info w ON t.id = w.tenant_id
            ${ew.customSqlSegment}
            """)
    IPage<TenantMngResponse> selectVOPage(IPage<TenantMngResponse> page, @Param(Constants.WRAPPER) Wrapper<TenantInfoDO> wrapper);

    @Select("""
            SELECT t.*, p.package_name, p.member_num_limit, p.post_num_limit, w.balance
            FROM biz.tenant_info t
            LEFT JOIN biz.tenant_package_info p ON t.package_id = p.id
            LEFT JOIN biz.tenant_wallet_info w ON t.id = w.tenant_id
            WHERE t.deleted = false AND t.id = #{tenantId}
            """)
    TenantMngResponse selectMngVOById(Long tenantId);

    @Select("""
            SELECT t.*, p.package_name
            FROM biz.tenant_info t
            LEFT JOIN biz.tenant_package_info p ON t.package_id = p.id
            WHERE t.deleted = false AND t.id = #{tenantId}
            """)
    TenantInfoResponse selectVOById(Long tenantId);

}

