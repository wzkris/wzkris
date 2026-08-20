package com.wzkris.usercenter.mapper;

import com.wzkris.common.orm.plus.BaseMapperPlus;
import com.wzkris.usercenter.domain.TenantRoleToMenuDO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface TenantRoleToMenuMapper extends BaseMapperPlus<TenantRoleToMenuDO> {

    @Select("""
            <script>
                SELECT menu_id FROM biz.tenant_role_to_menu WHERE tenant_role_id IN
                    <foreach collection="list" item="tenantRoleId" separator="," open="(" close=")">
                        #{tenantRoleId}
                    </foreach>
                    AND deleted = false
            </script>
            """)
    List<Long> listMenuIdByTenantRoleIds(List<Long> tenantRoleIds);

}
