package com.wzkris.usercenter.mapper;

import com.wzkris.common.orm.plus.BaseMapperPlus;
import com.wzkris.usercenter.domain.RoleToMenuDO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 角色与菜单关联表 数据层
 *
 * @author wzkris
 */
@Mapper
@Repository
public interface RoleToMenuMapper extends BaseMapperPlus<RoleToMenuDO> {

    /**
     * 根据角色id查出关联的所有菜单id
     *
     * @param roleIds 角色id集合
     * @return 菜单id集合
     */
    @Select("""
            <script>
                SELECT menu_id FROM biz.role_to_menu WHERE role_id IN
                    <foreach collection="list" item="roleId" separator="," open="(" close=")">
                        #{roleId}
                    </foreach>
                    AND deleted = false
            </script>
            """)
    List<Long> listMenuIdByRoleIds(List<Long> roleIds);

}
