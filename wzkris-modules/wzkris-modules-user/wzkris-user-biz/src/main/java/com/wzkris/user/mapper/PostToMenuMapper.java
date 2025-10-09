package com.wzkris.user.mapper;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface PostToMenuMapper {

    @Select("""
            <script>
                SELECT menu_id FROM biz.t_post_to_menu WHERE post_id IN
                    <foreach collection="list" item="postId" separator="," open="(" close=")">
                        #{postId}
                    </foreach>
            </script>
            """)
    List<Long> listMenuIdByPostIds(List<Long> postIds);

    @Delete("""
            <script>
                DELETE FROM biz.t_post_to_menu WHERE post_id IN
                    <foreach collection="list" item="postId" open="(" separator="," close=")">
                        #{postId}
                    </foreach>
            </script>
            """)
    int deleteByPostIds(List<Long> postIds);

}
