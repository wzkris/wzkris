package com.wzkris.principal.mapper;

import com.wzkris.principal.domain.MemberToPostDO;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface MemberToPostMapper {

    @Select("SELECT post_id FROM biz.member_to_post WHERE member_id = #{memberId}")
    List<Long> listPostIdByMemberId(Long memberId);

    @Insert("""
            <script>
                INSERT INTO biz.member_to_post(member_id, post_id) VALUES
                    <foreach collection="list" item="item" index="index" separator=",">
                        (#{item.memberId}, #{item.postId})
                    </foreach>
            </script>
            """)
    int insert(List<MemberToPostDO> list);

    @Delete("""
            <script>
                DELETE FROM biz.member_to_post WHERE member_id IN
                    <foreach collection="list" item="memberId" open="(" separator="," close=")">
                        #{memberId}
                    </foreach>
            </script>
            """)
    int deleteByMemberIds(List<Long> memberIds);

    default int deleteByMemberId(Long memberId) {
        return this.deleteByMemberIds(List.of(memberId));
    }

    @Delete("""
            <script>
                DELETE FROM biz.member_to_post WHERE post_id IN
                    <foreach collection="list" item="postId" open="(" separator="," close=")">
                        #{postId}
                    </foreach>
            </script>
            """)
    int deleteByPostIds(List<Long> postIds);

    @Select("""
            <script>
                SELECT EXISTS(
                    SELECT post_id FROM biz.member_to_post WHERE post_id IN
                        <foreach collection="list" item="postId" open="(" separator="," close=")">
                            #{postId}
                        </foreach>
                    )
            </script>
            """)
    boolean existByPostIds(List<Long> postIds);

}
