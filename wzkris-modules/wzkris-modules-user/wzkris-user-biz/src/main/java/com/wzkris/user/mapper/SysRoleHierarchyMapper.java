package com.wzkris.user.mapper;

import com.wzkris.user.domain.SysRoleHierarchy;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 角色继承表 数据层
 *
 * @author wzkris
 */
@Repository
public interface SysRoleHierarchyMapper {

    /**
     * 根据角色id查出继承的角色
     *
     * @param roleIds 角色id集合
     * @return 角色id集合
     */
    @Select("""
            <script>
                SELECT inherited_id FROM biz.sys_role_hierarchy WHERE role_id IN
                    <foreach collection="list" item="roleId" separator="," open="(" close=")">
                        #{roleId}
                    </foreach>
            </script>
            """)
    List<Long> listInheritedIdByRoleIds(List<Long> roleIds);

    @Insert("""
            <script>
                INSERT INTO biz.sys_role_hierarchy(role_id, inherited_id) VALUES
                    <foreach item="item" index="index" collection="list" separator=",">
                        (#{item.roleId}, #{item.inheritedId})
                    </foreach>
            </script>
            """)
    int insertBatch(List<SysRoleHierarchy> list);

    @Delete("DELETE FROM biz.sys_role_hierarchy WHERE role_id = #{roleId}")
    int deleteByRoleId(Long roleId);

    @Delete("""
            <script>
                DELETE FROM biz.sys_role_hierarchy WHERE role_id IN
                    <foreach collection="roleIds" item="roleId" open="(" separator="," close=")">
                        #{roleId}
                    </foreach>
            </script>
            """)
    int deleteByRoleIds(List<Long> roleIds);

    @Select("""
            SELECT EXISTS(SELECT 1 FROM biz.sys_role_hierarchy WHERE inherited_id = #{inheritedId})
            """)
    boolean existByInheritedId(Long inheritedId);

    @Select("""
            <script>
                SELECT EXISTS(
                    SELECT 1 FROM biz.sys_role_hierarchy WHERE inherited_id in
                    <foreach collection="roleIds" item="roleId" separator="," open="(" close=")">
                        #{roleId}
                    </foreach>
                )
            </script>
            """)
    boolean existByInheritedIds(List<Long> roleIds);

}
