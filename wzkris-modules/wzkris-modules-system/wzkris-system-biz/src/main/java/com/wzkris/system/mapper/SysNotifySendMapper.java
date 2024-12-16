package com.wzkris.system.mapper;

import com.wzkris.system.domain.SysNotifySend;
import org.apache.ibatis.annotations.Insert;
import org.springframework.stereotype.Repository;

import java.util.Collections;
import java.util.List;

/**
 * 通知发送表 数据层
 *
 * @author wzkris
 */
@Repository
public interface SysNotifySendMapper {

    @Insert("""
            <script>
                INSERT INTO sys_notify_send(user_id, notify_id, send_time, read_state) VALUES
                    <foreach collection="list" item="item" index="index" separator=",">
                        (#{item.userId}, #{item.msgId}, ${@java.lang.System@currentTimeMillis()}, #{readState})
                    </foreach>
            </script>
            """)
    int insert(List<SysNotifySend> list);

    default int insert(SysNotifySend notifySend) {
        return this.insert(Collections.singletonList(notifySend));
    }
}
