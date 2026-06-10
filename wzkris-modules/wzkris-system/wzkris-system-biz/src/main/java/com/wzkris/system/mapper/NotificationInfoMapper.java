package com.wzkris.system.mapper;

import com.wzkris.common.orm.plus.BaseMapperPlus;
import com.wzkris.system.api.notification.response.NotificationInfoResponse;
import com.wzkris.system.domain.NotificationInfoDO;
import jakarta.annotation.Nullable;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface NotificationInfoMapper extends BaseMapperPlus<NotificationInfoDO> {

    @Select("""
            <script>
                SELECT n.notification_id, n.title, n.content, s.read, n.create_at
                FROM biz.notification_to_admin s
                INNER JOIN biz.notification_info n ON s.notification_id = n.notification_id
                WHERE s.admin_id = #{adminId}
            	    <if test="notificationType != null and notificationType != ''">
            	        AND n.notification_type = #{notificationType}
            	    </if>
            	    <if test="read != null">
            	        AND s.read = #{read}
            	    </if>
                ORDER BY s.read ASC, s.notification_id DESC,
            </script>
            """)
    List<NotificationInfoResponse> listAdminNotice(
            @Param("adminId") Long adminId,
            @Nullable @Param("notificationType") String notificationType,
            @Nullable @Param("read") Boolean read);

    @Select("""
            <script>
                SELECT n.notification_id, n.title, n.content, s.read, n.create_at
                FROM biz.notification_to_tenant s
                INNER JOIN biz.notification_info n ON s.notification_id = n.notification_id
                WHERE s.member_id = #{memberId}
            	    <if test="notificationType != null and notificationType != ''">
            	        AND n.notification_type = #{notificationType}
            	    </if>
            	    <if test="read != null">
            	        AND s.read = #{read}
            	    </if>
                ORDER BY s.read ASC, s.notification_id DESC,
            </script>
            """)
    List<NotificationInfoResponse> listTenantNotice(
            @Param("memberId") Long memberId,
            @Nullable @Param("notificationType") String notificationType,
            @Nullable @Param("read") Boolean read);

    /**
     * 标记已读
     */
    @Update("UPDATE biz.notification_to_admin SET read = TRUE WHERE notification_id = #{notificationId} AND admin_id = #{adminId}")
    int updateAdminRead(@Param("notificationId") Long notificationId, @Param("adminId") Long adminId);

    /**
     * 最大统计100
     */
    @Select("""
            <script>
                SELECT COUNT(*) FROM
                (SELECT 1 FROM biz.notification_to_admin u INNER JOIN biz.notification_info n ON u.notification_id = n.notification_id
                WHERE u.admin_id = #{adminId} AND u.read = FALSE
                    <if test="notificationType != null and notificationType != ''">
            	        AND notification_type = #{notificationType}
            	    </if>
                LIMIT 100) tmp
            </script>
            """)
    int selectCountAdminUnread(@Param("adminId") Long adminId, @Nullable @Param("notificationType") String notificationType);

    /**
     * 租户端标记已读
     */
    @Update("UPDATE biz.notification_to_tenant SET read = TRUE WHERE notification_id = #{notificationId} AND member_id = #{memberId}")
    int updateTenantRead(@Param("notificationId") Long notificationId, @Param("memberId") Long memberId);

    /**
     * 租户端未读统计（最大统计100）
     */
    @Select("""
            <script>
                SELECT COUNT(*) FROM
                (SELECT 1 FROM biz.notification_to_tenant u INNER JOIN biz.notification_info n ON u.notification_id = n.notification_id
                WHERE u.member_id = #{memberId} AND u.read = FALSE
                    <if test="notificationType != null and notificationType != ''">
                        AND notification_type = #{notificationType}
                    </if>
                LIMIT 100) tmp
            </script>
            """)
    int selectCountTenantUnread(@Param("memberId") Long memberId, @Nullable @Param("notificationType") String notificationType);

}

