package com.wzkris.system.mapper;

import com.wzkris.common.orm.plus.BaseMapperPlus;
import com.wzkris.system.domain.SysNotice;
import com.wzkris.system.domain.vo.SysNoticeVO;
import jakarta.annotation.Nullable;
import java.util.List;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

@Repository
public interface SysNoticeMapper extends BaseMapperPlus<SysNotice> {

    @Select(
            """
            <script>
                SELECT * FROM biz.sys_notice_user s LEFT JOIN biz.sys_notice n ON s.notice_id = n.notice_id
                WHERE user_id = #{userId}
            	    <if test="noticeType != null and noticeType != ''">
            	        AND notice_type = #{noticeType}
            	    </if>
            	    <if test="readState != null and readState != ''">
            	        AND read_state = #{readState}
            	    </if>
                ORDER BY s.notice_id DESC
            </script>
            """)
    List<SysNoticeVO> listNotice(
            @Param("userId") Long userId,
            @Nullable @Param("noticeType") String noticeType,
            @Nullable @Param("readState") String readState);

    /**
     * 标记已读
     */
    @Update("UPDATE biz.sys_notice_user SET read_state = '1' WHERE notice_id = #{noticeId} AND user_id = #{userId}")
    int markRead(@Param("noticeId") Long noticeId, @Param("userId") Long userId);

    /**
     * 最大统计100
     */
    @Select(
            """
            <script>
                SELECT COUNT(*) FROM
                (SELECT 1 FROM biz.sys_notice_user u LEFT JOIN biz.sys_notice n ON u.notice_id = n.notice_id
                WHERE user_id = #{userId} AND read_state = '0'
                    <if test="noticeType != null and noticeType != ''">
            	        AND notice_type = #{noticeType}
            	    </if>
                LIMIT 100) tmp
            </script>
            """)
    int selectUnreadSize(@Param("userId") Long userId, @Nullable @Param("noticeType") String noticeType);
}
