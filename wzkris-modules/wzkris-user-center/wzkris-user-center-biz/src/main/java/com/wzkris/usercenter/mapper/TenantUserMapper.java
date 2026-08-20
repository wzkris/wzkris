package com.wzkris.usercenter.mapper;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.wzkris.common.orm.plus.BaseMapperPlus;
import com.wzkris.usercenter.api.tenantuser.response.TenantUserMngPageResponse;
import com.wzkris.usercenter.domain.TenantUserDO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface TenantUserMapper extends BaseMapperPlus<TenantUserDO> {

    @Select("""
            SELECT s.*, STRING_AGG(p.role_name, ',') AS role_name
            FROM biz.tenant_user s LEFT JOIN biz.tenant_user_to_role sp ON s.id = sp.tenant_user_id AND sp.deleted = false
             LEFT JOIN biz.tenant_role p ON sp.tenant_role_id = p.id AND p.status = '0'
            ${ew.customSqlSegment} GROUP BY s.id ORDER BY s.id DESC
            """)
    IPage<TenantUserMngPageResponse> selectVOPage(IPage<TenantUserMngPageResponse> page, @Param(Constants.WRAPPER) QueryWrapper<TenantUserDO> queryWrapper);

}

