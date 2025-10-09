package com.wzkris.user.mapper;

import com.wzkris.user.domain.UserToRoleDO;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.Collections;
import java.util.List;

/**
 * 用户与角色关联表 数据层
 *
 * @author wzkris
 */
@Mapper
@Repository
public interface UserToRoleMapper {

    /**
     * 根据角色id查出所有关联的管理员id
     *
     * @param roleId 角色id
     * @return 用户ID集合
     */
    @Select("SELECT user_id FROM biz.user_to_role WHERE role_id = #{roleId}")
    List<Long> listUserIdByRoleId(Long roleId);

    /**
     * 根据用户id查出所有关联的角色id
     *
     * @param userId 用户id
     * @return 用户ID集合
     */
    @Select("SELECT role_id FROM biz.user_to_role WHERE user_id = #{userId}")
    List<Long> listRoleIdByUserId(Long userId);

    /**
     * 通过用户ID删除用户和角色关联
     *
     * @param userId 用户ID
     * @return 结果
     */
    default int deleteByUserId(Long userId) {
        return this.deleteByUserIds(List.of(userId));
    }

    /**
     * 批量删除用户和角色关联
     *
     * @param userIds 需要删除的数据ID
     * @return 结果
     */
    @Delete("""
            <script>
                DELETE FROM biz.user_to_role WHERE user_id IN
                    <foreach collection="list" item="userId" open="(" separator="," close=")">
                        #{userId}
                    </foreach>
            </script>
            """)
    int deleteByUserIds(List<Long> userIds);

    /**
     * 批量删除用户和角色关联
     *
     * @param roleIds 需要删除的数据ID
     * @return 结果
     */
    @Delete("""
            <script>
                DELETE FROM biz.user_to_role WHERE role_id IN
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
                    SELECT role_id FROM biz.user_to_role WHERE role_id IN
                        <foreach collection="list" item="roleId" open="(" separator="," close=")">
                            #{roleId}
                        </foreach>
                    )
            </script>
            """)
    boolean existByRoleIds(List<Long> roleIds);

    /**
     * 批量新增用户角色信息
     *
     * @param list 用户角色列表
     * @return 结果
     */
    @Insert("""
            <script>
                INSERT INTO biz.user_to_role(user_id, role_id) VALUES
                    <foreach collection="list" item="item" index="index" separator=",">
                        (#{item.userId}, #{item.roleId})
                    </foreach>
            </script>
            """)
    int insert(List<UserToRoleDO> list);

    default int insert(UserToRoleDO userToRoleDO) {
        return this.insert(Collections.singletonList(userToRoleDO));
    }

    /**
     * 删除
     *
     * @return 结果
     */
    @Delete("DELETE FROM biz.user_to_role WHERE user_id = #{userId} AND role_id = #{roleId}")
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
                DELETE FROM biz.user_to_role WHERE role_id = #{roleId} AND user_id IN
                    <foreach collection="userIds" item="userId" open="(" separator="," close=")">
                        #{userId}
                    </foreach>
            </script>
            """)
    int deleteBatch(@Param("roleId") Long roleId, @Param("userIds") List<Long> userIds);

}
