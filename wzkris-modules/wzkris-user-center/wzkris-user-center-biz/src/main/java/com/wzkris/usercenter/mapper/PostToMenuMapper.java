package com.wzkris.usercenter.mapper;

import com.wzkris.common.orm.plus.BaseMapperPlus;
import com.wzkris.usercenter.domain.PostToMenuDO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface PostToMenuMapper extends BaseMapperPlus<PostToMenuDO> {

    @Select("""
            <script>
                SELECT menu_id FROM biz.post_to_menu WHERE post_id IN
                    <foreach collection="list" item="postId" separator="," open="(" close=")">
                        #{postId}
                    </foreach>
                    AND deleted = false
            </script>
            """)
    List<Long> listMenuIdByPostIds(List<Long> postIds);

}
