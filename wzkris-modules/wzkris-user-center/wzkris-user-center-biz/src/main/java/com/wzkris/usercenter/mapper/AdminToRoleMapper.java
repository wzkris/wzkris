package com.wzkris.usercenter.mapper;

import com.wzkris.usercenter.domain.AdminToRoleDO;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.Collections;
import java.util.List;

/**
 * 管理员和角色关联表 数据层
 *
 * @author wzkris
 */
@Mapper
@Repository
public interface AdminToRoleMapper {

    /**
     * 根据角色id查出所有关联的管理员id
     *
     * @param roleId 角色id
     * @return 管理员ID集合
     */
    @Select("SELECT admin_id FROM biz.admin_to_role WHERE role_id = #{roleId}")
    List<Long> listAdminIdByRoleId(Long roleId);

    /**
     * 根据管理员id查出所有关联的角色id
     *
     * @param adminId 管理员id
     * @return 管理员ID集合
     */
    @Select("SELECT role_id FROM biz.admin_to_role WHERE admin_id = #{adminId}")
    List<Long> listRoleIdByAdminId(Long adminId);

    /**
     * 通过管理员ID删除管理员和角色关联
     *
     * @param adminId 管理员ID
     * @return 结果
     */
    default int deleteByAdminId(Long adminId) {
        return this.deleteByAdminIds(List.of(adminId));
    }

    /**
     * 批量删除管理员和角色关联
     *
     * @param adminIds 需要删除的数据ID
     * @return 结果
     */
    @Delete("""
            <script>
                DELETE FROM biz.admin_to_role WHERE admin_id IN
                    <foreach collection="list" item="adminId" open="(" separator="," close=")">
                        #{adminId}
                    </foreach>
            </script>
            """)
    int deleteByAdminIds(List<Long> adminIds);

    /**
     * 批量删除管理员和角色关联
     *
     * @param roleIds 需要删除的数据ID
     * @return 结果
     */
    @Delete("""
            <script>
                DELETE FROM biz.admin_to_role WHERE role_id IN
                    <foreach collection="list" item="roleId" open="(" separator="," close=")">
                        #{roleId}
                    </foreach>
            </script>
            """)
    int deleteByRoleIds(List<Long> roleIds);

    /**
     * 通过角色ID查询角色使用数量
     *
     * @param roleIds 角色ID
     * @return 结果
     */
    @Select("""
            <script>
                SELECT EXISTS(
                    SELECT role_id FROM biz.admin_to_role WHERE role_id IN
                        <foreach collection="list" item="roleId" open="(" separator="," close=")">
                            #{roleId}
                        </foreach>
                    )
            </script>
            """)
    boolean existByRoleIds(List<Long> roleIds);

    /**
     * 批量新增管理员角色信息
     *
     * @param list 管理员角色列表
     * @return 结果
     */
    @Insert("""
            <script>
                INSERT INTO biz.admin_to_role(admin_id, role_id) VALUES
                    <foreach collection="list" item="item" index="index" separator=",">
                        (#{item.adminId}, #{item.roleId})
                    </foreach>
            </script>
            """)
    int insert(List<AdminToRoleDO> list);

    default int insert(AdminToRoleDO adminToRoleDO) {
        return this.insert(Collections.singletonList(adminToRoleDO));
    }

    /**
     * 删除
     *
     * @return 结果
     */
    @Delete("DELETE FROM biz.admin_to_role WHERE admin_id = #{adminId} AND role_id = #{roleId}")
    int delete(@Param("adminId") Long adminId, @Param("roleId") Long roleId);

    /**
     * 批量删除
     *
     * @param roleId   角色ID
     * @param adminIds 需要删除的管理员数据ID
     * @return 结果
     */
    @Delete("""
            <script>
                DELETE FROM biz.admin_to_role WHERE role_id = #{roleId} AND admin_id IN
                    <foreach collection="adminIds" item="adminId" open="(" separator="," close=")">
                        #{adminId}
                    </foreach>
            </script>
            """)
    int deleteBatch(@Param("roleId") Long roleId, @Param("adminIds") List<Long> adminIds);

}
