package com.thingslink.auth.mapper;

import com.thingslink.auth.domain.SysMenu;
import com.thingslink.common.orm.plus.BaseMapperPlus;
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
                SELECT perms FROM sys_menu WHERE status = '0' AND menu_id IN
                    <foreach collection="menuIds" item="menuId" separator="," open="(" close=")">
                        #{menuId}
                    </foreach>
            </script>
            """)
    List<String> listPermsByMenuIds(@Param("menuIds") List<Long> menuIds);

    /**
     * 是否存在菜单子节点
     *
     * @param menuId 菜单ID
     * @return 结果
     */
    int hasChildByMenuId(Long menuId);

    /**
     * 校验菜单名称是否唯一
     *
     * @param menuName 菜单名称
     * @param parentId 父菜单ID
     * @return 结果
     */
    SysMenu checkMenuNameUnique(@Param("menuName") String menuName, @Param("parentId") Long parentId);

}
