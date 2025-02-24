package com.wzkris.user.mapper;

import com.wzkris.common.orm.plus.BaseMapperPlus;
import com.wzkris.user.domain.SysMenu;
import jakarta.annotation.Nullable;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 菜单表 数据层
 *
 * @author wzkris
 */
@Repository
public interface SysMenuMapper extends BaseMapperPlus<SysMenu> {

    /**
     * 查询前端可见的菜单(按钮除外)
     *
     * @return 菜单列表
     */
    List<SysMenu> listRouteTree(@Nullable @Param("menuIds") List<Long> menuIds);

    /**
     * 根据ID集合查询权限
     *
     * @param menuIds 角色ID集合
     * @return 权限列表
     */
    @Select("""
            <script>
                SELECT perms FROM biz_sys.sys_menu WHERE status = '0' AND menu_id IN
                    <foreach collection="menuIds" item="menuId" separator="," open="(" close=")">
                        #{menuId}
                    </foreach>
            </script>
            """)
    List<String> listPermsByMenuIds(@Param("menuIds") List<Long> menuIds);

}
