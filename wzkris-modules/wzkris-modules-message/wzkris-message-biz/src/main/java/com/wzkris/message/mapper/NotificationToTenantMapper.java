package com.wzkris.message.mapper;

import com.wzkris.message.domain.NotificationToTenantDO;
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
public interface NotificationToTenantMapper {

    @Insert("""
            <script>
                INSERT INTO biz.notification_to_tenant(notification_id, member_id, read) VALUES
                    <foreach collection="list" item="item" index="index" separator=",">
                        (#{item.notificationId},  #{item.memberId},  #{item.read})
                    </foreach>
            </script>
            """)
    int insert(List<NotificationToTenantDO> list);

    default int insert(NotificationToTenantDO notification) {
        return this.insert(Collections.singletonList(notification));
    }

}
