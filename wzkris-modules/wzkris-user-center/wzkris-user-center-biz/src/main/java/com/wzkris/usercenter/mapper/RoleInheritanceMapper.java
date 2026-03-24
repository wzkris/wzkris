package com.wzkris.usercenter.mapper;

import com.wzkris.usercenter.domain.RoleInheritanceDO;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.Collections;
import java.util.List;

/**
 * 角色继承关系关联表 数据层
 *
 * @author wzkris
 */
@Mapper
@Repository
public interface RoleInheritanceMapper {

    /**
     * 根据角色 ID 查询所有子角色 ID
     *
     * @param roleId 角色 ID
     * @return 子角色 ID 列表
     */
    @Select("SELECT child_id FROM biz.role_inheritance WHERE role_id = #{roleId}")
    List<Long> listChildIdsByRoleId(Long roleId);

    /**
     * 批量查询角色的所有子角色 ID（包括递归）
     *
     * @param roleIds 角色 ID 列表
     * @return 子角色 ID 列表
     */
    @Select("""
            <script>
                WITH RECURSIVE child_roles AS (
                    SELECT child_id FROM biz.role_inheritance WHERE role_id IN
                        <foreach collection="list" item="roleId" separator="," open="(" close=")">
                            #{roleId}
                        </foreach>
                    UNION
                    SELECT ri.child_id FROM biz.role_inheritance ri
                    INNER JOIN child_roles cr ON ri.role_id = cr.child_id
                )
                SELECT DISTINCT child_id FROM child_roles
            </script>
            """)
    List<Long> listChildIdsRecursive(List<Long> roleIds);

    /**
     * 通过角色 ID 删除角色继承关联（作为角色）
     *
     * @param roleId 角色 ID
     * @return 结果
     */
    default int deleteByRoleId(Long roleId) {
        return this.deleteByRoleIds(List.of(roleId));
    }

    /**
     * 批量删除角色继承关联信息（作为角色）
     *
     * @param roleIds 角色 id 集合
     * @return 结果
     */
    @Delete("""
            <script>
                DELETE FROM biz.role_inheritance WHERE role_id IN
                    <foreach collection="list" item="roleId" open="(" separator="," close=")">
                        #{roleId}
                    </foreach>
            </script>
            """)
    int deleteByRoleIds(List<Long> roleIds);

    /**
     * 批量删除角色继承关联信息（作为子角色）
     *
     * @param roleIds 角色 id 集合
     * @return 结果
     */
    @Delete("""
            <script>
                DELETE FROM biz.role_inheritance WHERE child_id IN
                    <foreach collection="list" item="roleId" open="(" separator="," close=")">
                        #{roleId}
                    </foreach>
            </script>
            """)
    int deleteByChildIds(List<Long> roleIds);

    /**
     * 检查角色是否被其他角色继承
     *
     * @param roleIds 角色 ID 列表
     * @return 是否存在
     */
    @Select("""
            <script>
                SELECT COUNT(1) FROM biz.role_inheritance WHERE child_id IN
                    <foreach collection="list" item="roleId" separator="," open="(" close=")">
                        #{roleId}
                    </foreach>
                LIMIT 1
            </script>
            """)
    int existChildRole(List<Long> roleIds);

    /**
     * 批量新增角色继承关系
     *
     * @param list 角色继承关系列表
     * @return 结果
     */
    @Insert("""
            <script>
                INSERT INTO biz.role_inheritance(role_id, child_id) VALUES
                    <foreach collection="list" item="item" index="index" separator=",">
                        (#{item.roleId}, #{item.childId})
                    </foreach>
            </script>
            """)
    int insert(List<RoleInheritanceDO> list);

    default int insert(RoleInheritanceDO inheritance) {
        return this.insert(Collections.singletonList(inheritance));
    }

}
