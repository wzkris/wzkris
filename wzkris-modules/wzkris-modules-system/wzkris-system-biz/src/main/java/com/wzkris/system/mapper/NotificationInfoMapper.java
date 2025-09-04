package com.wzkris.system.mapper;

import com.wzkris.common.orm.plus.BaseMapperPlus;
import com.wzkris.system.domain.NotificationInfoDO;
import com.wzkris.system.domain.vo.NotificationInfoVO;
import jakarta.annotation.Nullable;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificationInfoMapper extends BaseMapperPlus<NotificationInfoDO> {

    @Select("""
            <script>
                SELECT * FROM biz.notification_to_user s LEFT JOIN biz.notification_info n ON s.notification_id = n.notification_id
                WHERE user_id = #{userId}
            	    <if test="notificationType != null and notificationType != ''">
            	        AND notification_type = #{notificationType}
            	    </if>
            	    <if test="readState != null and readState != ''">
            	        AND read_state = #{readState}
            	    </if>
                ORDER BY s.notification_id DESC
            </script>
            """)
    List<NotificationInfoVO> listNotice(
            @Param("userId") Long userId,
            @Nullable @Param("notificationType") String notificationType,
            @Nullable @Param("readState") String readState);

    /**
     * 标记已读
     */
    @Update("UPDATE biz.notification_to_user SET read_state = '1' WHERE notification_id = #{notificationId} AND user_id = #{userId}")
    int markRead(@Param("notificationId") Long notificationId, @Param("userId") Long userId);

    /**
     * 最大统计100
     */
    @Select("""
            <script>
                SELECT COUNT(*) FROM
                (SELECT 1 FROM biz.notification_to_user u LEFT JOIN biz.notification_info n ON u.notification_id = n.notification_id
                WHERE user_id = #{userId} AND read_state = '0'
                    <if test="notificationType != null and notificationType != ''">
            	        AND notification_type = #{notificationType}
            	    </if>
                LIMIT 100) tmp
            </script>
            """)
    int countUnread(@Param("userId") Long userId, @Nullable @Param("notificationType") String notificationType);

}
