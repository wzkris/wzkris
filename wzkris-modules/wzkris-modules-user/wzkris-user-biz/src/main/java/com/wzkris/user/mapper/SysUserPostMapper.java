package com.wzkris.user.mapper;

import com.wzkris.user.domain.SysUserPost;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 用户与岗位关联表 数据层
 *
 * @author wzkris
 */
@Repository
public interface SysUserPostMapper {

    /**
     * 通过用户ID查询所有岗位id
     *
     * @param userId 用户ID
     * @return 结果
     */
    @Select("SELECT post_id FROM biz_sys.sys_user_post WHERE user_id = #{userId}")
    List<Long> listPostIdByUserId(Long userId);

    /**
     * 通过用户ID删除用户和岗位关联
     *
     * @param userId 用户ID
     * @return 结果
     */
    @Delete("DELETE FROM biz_sys.sys_user_post WHERE user_id = #{userId}")
    int deleteByUserId(Long userId);

    /**
     * 通过岗位ID查询岗位使用数量
     *
     * @param postIds 岗位ID
     * @return 结果
     */
    @Select("""
            <script>
                SELECT EXISTS( 
                    SELECT post_id FROM biz_sys.sys_user_post WHERE post_id IN
                        <foreach collection="list" item="postId" open="(" separator="," close=")">
                            #{postId}
                        </foreach>
                    )
            </script>
            """)
    boolean checkExistByPostIds(List<Long> postIds);

    /**
     * 批量删除用户和岗位关联
     *
     * @param userIds 用户id集合
     * @return 结果
     */
    @Delete("""
            <script>
                DELETE FROM biz_sys.sys_user_post WHERE user_id IN
                    <foreach collection="list" item="userId" open="(" separator="," close=")">
                        #{userId}
                    </foreach>
            </script>
            """)
    int deleteByUserIds(List<Long> userIds);

    /**
     * 批量删除用户和岗位关联
     *
     * @param postIds 岗位id集合
     * @return 结果
     */
    @Delete("""
            <script>
                DELETE FROM biz_sys.sys_user_post WHERE post_id IN
                    <foreach collection="list" item="postId" open="(" separator="," close=")">
                        #{postId}
                    </foreach>
            </script>
            """)
    int deleteByPostIds(List<Long> postIds);

    /**
     * 批量新增用户岗位信息
     *
     * @param userPosts 用户角色列表
     * @return 结果
     */
    @Insert("""
            <script>
                INSERT INTO biz_sys.sys_user_post(user_id, post_id) VALUES
                    <foreach collection="list" item="item" index="index" separator=",">
                        (#{item.userId}, #{item.postId})
                    </foreach>
            </script>
            """)
    int insertBatch(List<SysUserPost> userPosts);

}
