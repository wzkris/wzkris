package com.wzkris.usercenter.mapper;

import com.wzkris.usercenter.domain.PostToMenuDO;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface PostToMenuMapper {

    @Select("""
            <script>
                SELECT menu_id FROM biz.post_to_menu WHERE post_id IN
                    <foreach collection="list" item="postId" separator="," open="(" close=")">
                        #{postId}
                    </foreach>
            </script>
            """)
    List<Long> listMenuIdByPostIds(List<Long> postIds);

    @Insert("""
            <script>
                INSERT INTO biz.post_to_menu(post_id, menu_id) VALUES
                    <foreach item="item" index="index" collection="list" separator=",">
                        (#{item.postId}, #{item.menuId})
                    </foreach>
            </script>
            """)
    int insert(List<PostToMenuDO> list);

    @Delete("""
            <script>
                DELETE FROM biz.post_to_menu WHERE post_id IN
                    <foreach collection="list" item="postId" open="(" separator="," close=")">
                        #{postId}
                    </foreach>
            </script>
            """)
    int deleteByPostIds(List<Long> postIds);

    default int deleteByPostId(Long postId) {
        return this.deleteByPostIds(List.of(postId));
    }

    @Delete("DELETE FROM biz.post_to_menu WHERE menu_id = #{menuId}")
    int deleteByMenuId(Long menuId);

}
