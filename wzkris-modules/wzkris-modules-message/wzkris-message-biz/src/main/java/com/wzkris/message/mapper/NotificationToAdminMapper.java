package com.wzkris.message.mapper;

import com.wzkris.message.domain.NotificationToAdminDO;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.Collections;
import java.util.List;

/**
 * 通知发送表 数据层
 *
 * @author wzkris
 */
@Mapper
@Repository
public interface NotificationToAdminMapper {

    @Insert("""
            <script>
                INSERT INTO biz.notification_to_admin(notification_id, admin_id, read) VALUES
                    <foreach collection="list" item="item" index="index" separator=",">
                        (#{item.notificationId},  #{item.adminId},  #{item.read})
                    </foreach>
            </script>
            """)
    int insert(List<NotificationToAdminDO> list);

    default int insert(NotificationToAdminDO notification) {
        return this.insert(Collections.singletonList(notification));
    }

}
