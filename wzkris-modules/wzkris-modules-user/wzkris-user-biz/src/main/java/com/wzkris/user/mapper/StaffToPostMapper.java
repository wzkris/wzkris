package com.wzkris.user.mapper;

import com.wzkris.user.domain.StaffToPostDO;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface StaffToPostMapper {

    @Select("SELECT post_id FROM biz.t_staff_to_post WHERE staff_id = #{staffId}")
    List<Long> listPostIdByStaffId(Long staffId);

    @Insert("""
            <script>
                INSERT INTO biz.t_staff_to_post(staff_id, post_id) VALUES
                    <foreach collection="list" item="item" index="index" separator=",">
                        (#{item.staffId}, #{item.postId})
                    </foreach>
            </script>
            """)
    int insert(List<StaffToPostDO> list);

    @Delete("""
            <script>
                DELETE FROM biz.t_staff_to_post WHERE staff_id IN
                    <foreach collection="list" item="staffId" open="(" separator="," close=")">
                        #{staffId}
                    </foreach>
            </script>
            """)
    int deleteByStaffIds(List<Long> staffIds);

    default int deleteByStaffId(Long staffId) {
        return this.deleteByStaffIds(List.of(staffId));
    }

    @Delete("""
            <script>
                DELETE FROM biz.t_staff_to_post WHERE post_id IN
                    <foreach collection="list" item="postId" open="(" separator="," close=")">
                        #{postId}
                    </foreach>
            </script>
            """)
    int deleteByPostIds(List<Long> postIds);

}
