package com.wzkris.user.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.wzkris.common.orm.plus.BaseMapperPlus;
import com.wzkris.user.domain.TenantInfoDO;
import com.wzkris.user.domain.vo.tenant.TenantInfoVO;
import com.wzkris.user.domain.vo.tenant.TenantManageVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 租户表 数据层
 *
 * @author wzkris
 */
@Mapper
@Repository
public interface TenantInfoMapper extends BaseMapperPlus<TenantInfoDO> {

    @Select("SELECT oper_pwd FROM biz.tenant_info WHERE tenant_id = #{tenantId}")
    String selectOperPwdById(Long tenantId);

    @Select("""
            SELECT t.*, p.package_name, w.balance FROM biz.tenant_info t LEFT JOIN biz.tenant_package_info p ON t.package_id = p.package_id
            LEFT JOIN biz.tenant_wallet_info w ON t.tenant_id = w.tenant_id
            ${ew.customSqlSegment}
            """)
    List<TenantManageVO> selectVOList(@Param(Constants.WRAPPER) Wrapper<TenantInfoDO> wrapper);

    /**
     * 根据用户ID查询套餐ID，如果查到则说明是租户最高管理员
     *
     * @param userId 用户ID
     * @return 套餐ID
     */
    @Select("SELECT package_id FROM biz.tenant_info WHERE administrator = #{userId}")
    Long selectPackageIdByUserId(Long userId);

    @Select("""
             SELECT t.*, p.package_name FROM biz.tenant_info t LEFT JOIN biz.tenant_package_info p ON t.package_id = p.package_id
                        WHERE t.tenant_id = #{tenantId}
            """)
    TenantInfoVO selectVOById(Long tenantId);

}
