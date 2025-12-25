package com.wzkris.system.mapper;

import com.wzkris.common.orm.plus.BaseMapperPlus;
import com.wzkris.system.domain.NotificationInfoDO;
import com.wzkris.system.domain.vo.notification.NotificationInfoVO;
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
                SELECT * FROM biz.notification_to_admin s LEFT JOIN biz.notification_info n ON s.notification_id = n.notification_id
                WHERE admin_id = #{adminId}
            	    <if test="notificationType != null and notificationType != ''">
            	        AND notification_type = #{notificationType}
            	    </if>
            	    <if test="read != null and read != ''">
            	        AND read = #{read}
            	    </if>
                ORDER BY s.notification_id DESC
            </script>
            """)
    List<NotificationInfoVO> listAdminNotice(
            @Param("adminId") Long adminId,
            @Nullable @Param("notificationType") String notificationType,
            @Nullable @Param("read") String read);

    @Select("""
            <script>
                SELECT * FROM biz.notification_to_tenant s LEFT JOIN biz.notification_info n ON s.notification_id = n.notification_id
                WHERE member_id = #{memberId}
            	    <if test="notificationType != null and notificationType != ''">
            	        AND notification_type = #{notificationType}
            	    </if>
            	    <if test="read != null and read != ''">
            	        AND read = #{read}
            	    </if>
                ORDER BY s.notification_id DESC
            </script>
            """)
    List<NotificationInfoVO> listTenantNotice(
            @Param("memberId") Long memberId,
            @Nullable @Param("notificationType") String notificationType,
            @Nullable @Param("read") String read);

    /**
     * 标记已读
     */
    @Update("UPDATE biz.notification_to_admin SET read = '1' WHERE notification_id = #{notificationId} AND admin_id = #{adminId}")
    int markAdminRead(@Param("notificationId") Long notificationId, @Param("adminId") Long adminId);

    /**
     * 最大统计100
     */
    @Select("""
            <script>
                SELECT COUNT(*) FROM
                (SELECT 1 FROM biz.notification_to_admin u LEFT JOIN biz.notification_info n ON u.notification_id = n.notification_id
                WHERE admin_id = #{adminId} AND read = '0'
                    <if test="notificationType != null and notificationType != ''">
            	        AND notification_type = #{notificationType}
            	    </if>
                LIMIT 100) tmp
            </script>
            """)
    int countAdminUnread(@Param("adminId") Long adminId, @Nullable @Param("notificationType") String notificationType);

    /**
     * 租户端标记已读
     */
    @Update("UPDATE biz.notification_to_tenant SET read = '1' WHERE notification_id = #{notificationId} AND member_id = #{memberId}")
    int markTenantRead(@Param("notificationId") Long notificationId, @Param("memberId") Long memberId);

    /**
     * 租户端未读统计（最大统计100）
     */
    @Select("""
            <script>
                SELECT COUNT(*) FROM
                (SELECT 1 FROM biz.notification_to_tenant u LEFT JOIN biz.notification_info n ON u.notification_id = n.notification_id
                WHERE member_id = #{memberId} AND read = '0'
                    <if test="notificationType != null and notificationType != ''">
                        AND notification_type = #{notificationType}
                    </if>
                LIMIT 100) tmp
            </script>
            """)
    int countTenantUnread(@Param("memberId") Long memberId, @Nullable @Param("notificationType") String notificationType);

}
