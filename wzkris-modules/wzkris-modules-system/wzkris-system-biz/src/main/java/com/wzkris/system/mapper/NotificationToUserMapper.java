package com.wzkris.system.mapper;

import com.wzkris.system.domain.NotificationToUserDO;
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
public interface NotificationToUserMapper {

    @Insert("""
            <script>
                INSERT INTO biz.notification_to_user(notification_id, user_id, read) VALUES
                    <foreach collection="list" item="item" index="index" separator=",">
                        (#{item.notificationId},  #{item.userId},  #{item.read})
                    </foreach>
            </script>
            """)
    int insert(List<NotificationToUserDO> list);

    default int insert(NotificationToUserDO notification) {
        return this.insert(Collections.singletonList(notification));
    }

}
