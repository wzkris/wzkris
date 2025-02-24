package com.wzkris.user.mapper;

import com.wzkris.user.domain.SysRoleMenu;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 角色与菜单关联表 数据层
 *
 * @author wzkris
 */
@Repository
public interface SysRoleMenuMapper {

    /**
     * 根据角色id查出关联的所有菜单id
     *
     * @param roleIds 角色id集合
     * @return 菜单id集合
     */
    @Select("""
            <script>
                SELECT menu_id FROM biz_sys.sys_role_menu WHERE role_id IN
                    <foreach collection="roleIds" item="roleId" separator="," open="(" close=")">
                        #{roleId}
                    </foreach>
            </script>
            """)
    List<Long> listMenuIdByRoleIds(@Param("roleIds") List<Long> roleIds);

    /**
     * 查询菜单是否使用
     *
     * @param menuId 菜单ID
     * @return 结果
     */
    @Select("SELECT EXISTS(SELECT * FROM biz_sys.sys_role_menu WHERE menu_id = #{menuId})")
    int checkMenuExistRole(Long menuId);

    /**
     * 通过角色ID删除角色和菜单关联
     *
     * @param roleId 角色ID
     * @return 结果
     */
    @Delete("DELETE FROM biz_sys.sys_role_menu WHERE role_id = #{roleId}")
    int deleteByRoleId(Long roleId);

    /**
     * 批量删除角色菜单关联信息
     *
     * @param roleIds 角色id集合
     * @return 结果
     */
    @Delete("""
            <script>
                DELETE FROM biz_sys.sys_role_menu WHERE role_id IN
                    <foreach collection="roleIds" item="roleId" open="(" separator="," close=")">
                        #{roleId}
                    </foreach>
            </script>
            """)
    int deleteByRoleIds(@Param("roleIds") List<Long> roleIds);

    /**
     * 通过菜单ID删除角色和菜单关联
     *
     * @param menuId 菜单ID
     * @return 结果
     */
    @Delete("DELETE FROM biz_sys.sys_role_menu WHERE menu_id = #{menuId}")
    int deleteByMenuId(Long menuId);

    /**
     * 批量新增角色菜单信息
     *
     * @param sysRoleMenuList 角色菜单列表
     * @return 结果
     */
    @Insert("""
            <script>
                INSERT INTO biz_sys.sys_role_menu(role_id, menu_id) VALUES
                    <foreach collection="list" item="item" index="index" separator=",">
                        (#{item.roleId}, #{item.menuId})
                    </foreach>
            </script>
            """)
    int insertBatch(List<SysRoleMenu> sysRoleMenuList);

}
