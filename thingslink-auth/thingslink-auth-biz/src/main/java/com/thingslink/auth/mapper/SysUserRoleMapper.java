package com.thingslink.auth.mapper;

import com.thingslink.auth.domain.SysUserRole;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 用户与角色关联表 数据层
 *
 * @author wzkris
 */
@Repository
public interface SysUserRoleMapper {


    /**
     * 根据角色id查出所有关联的管理员id
     *
     * @param roleId 角色id
     * @return 用户ID集合
     */
    @Select("SELECT user_id FROM sys_user_role WHERE role_id = #{roleId}")
    List<Long> listUserIdByRoleId(Long roleId);

    /**
     * 根据用户id查出所有关联的角色id
     *
     * @param userId 用户id
     * @return 用户ID集合
     */
    @Select("SELECT role_id FROM sys_user_role WHERE user_id = #{userId}")
    List<Long> listRoleIdByUserId(Long userId);

    /**
     * 通过用户ID删除用户和角色关联
     *
     * @param userId 用户ID
     * @return 结果
     */
    @Delete("DELETE FROM sys_user_role WHERE user_id = #{userId}")
    int deleteByUserId(Long userId);

    /**
     * 批量删除用户和角色关联
     *
     * @param userIds 需要删除的数据ID
     * @return 结果
     */
    @Delete("""
            <script>
                DELETE FROM sys_user_role WHERE user_id IN
                    <foreach collection="array" item="userId" open="(" separator="," close=")">
                        #{userId}
                    </foreach>
            </script>
            """)
    int deleteBatchByUserIds(Long... userIds);

    /**
     * 通过角色ID查询角色使用数量
     *
     * @param roleIds 角色ID
     * @return 结果
     */
    @Select("""
            <script>
                SELECT COUNT(*) FROM sys_user_role WHERE role_id IN
                    <foreach collection="array" item="roleId" open="(" separator="," close=")">
                        #{roleId}
                    </foreach>
            </script>
            """)
    int countByRoleIds(Long[] roleIds);

    /**
     * 批量新增用户角色信息
     *
     * @param sysUserRoleList 用户角色列表
     * @return 结果
     */
    @Insert("""
            <script>
                INSERT INTO sys_user_role(user_id, role_id) VALUES
                    <foreach item="item" index="index" collection="list" separator=",">
                        (#{item.userId}, #{item.roleId})
                    </foreach>
            </script>
            """)
    int insertBatch(List<SysUserRole> sysUserRoleList);

    /**
     * 删除
     *
     * @return 结果
     */
    @Delete("DELETE FROM sys_user_role WHERE user_id = #{userId} AND role_id = #{roleId}")
    int delete(@Param("userId") Long userId, @Param("roleId") Long roleId);

    /**
     * 批量删除
     *
     * @param roleId  角色ID
     * @param userIds 需要删除的用户数据ID
     * @return 结果
     */
    @Delete("""
            <script>
                DELETE FROM sys_user_role WHERE role_id = #{roleId} AND user_id IN
                    <foreach collection="userIds" item="userId" open="(" separator="," close=")">
                        #{userId}
                    </foreach>
            </script>
            """)
    int deleteBatch(@Param("roleId") Long roleId, @Param("userIds") Long[] userIds);

}
