package com.wzkris.user.mapper;

import com.wzkris.user.domain.RoleToMenuDO;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 角色与菜单关联表 数据层
 *
 * @author wzkris
 */
@Repository
public interface RoleToMenuMapper {

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
            </script>
            """)
    List<Long> listMenuIdByRoleIds(List<Long> roleIds);

    /**
     * 查询菜单是否使用
     *
     * @param menuId 菜单ID
     * @return 结果
     */
    @Select("SELECT EXISTS(SELECT * FROM biz.role_to_menu WHERE menu_id = #{menuId})")
    boolean existByMenuId(Long menuId);

    /**
     * 通过角色ID删除角色和菜单关联
     *
     * @param roleId 角色ID
     * @return 结果
     */
    @Delete("DELETE FROM biz.role_to_menu WHERE role_id = #{roleId}")
    int deleteByRoleId(Long roleId);

    /**
     * 批量删除角色菜单关联信息
     *
     * @param roleIds 角色id集合
     * @return 结果
     */
    @Delete("""
            <script>
                DELETE FROM biz.role_to_menu WHERE role_id IN
                    <foreach collection="list" item="roleId" open="(" separator="," close=")">
                        #{roleId}
                    </foreach>
            </script>
            """)
    int deleteByRoleIds(List<Long> roleIds);

    /**
     * 通过菜单ID删除角色和菜单关联
     *
     * @param menuId 菜单ID
     * @return 结果
     */
    @Delete("DELETE FROM biz.role_to_menu WHERE menu_id = #{menuId}")
    int deleteByMenuId(Long menuId);

    /**
     * 批量新增角色菜单信息
     *
     * @param roleMenus 角色菜单列表
     * @return 结果
     */
    @Insert("""
            <script>
                INSERT INTO biz.role_to_menu(role_id, menu_id) VALUES
                    <foreach collection="list" item="item" index="index" separator=",">
                        (#{item.roleId}, #{item.menuId})
                    </foreach>
            </script>
            """)
    int insertBatch(List<RoleToMenuDO> roleMenus);

}
