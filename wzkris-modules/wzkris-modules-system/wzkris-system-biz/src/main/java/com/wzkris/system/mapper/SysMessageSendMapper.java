package com.wzkris.system.mapper;

import com.wzkris.system.domain.SysMessageSend;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.Collections;
import java.util.List;

/**
 * 通知发送表 数据层
 *
 * @author wzkris
 */
@Repository
public interface SysMessageSendMapper {

    @Select("SELECT COUNT(*) FROM sys_message_send WHERE user_id = #{userId} AND read_state = '0'")
    int countUnreadByUserId(Long userId);

    @Insert("""
            <script>
                INSERT INTO sys_message_send(user_id, notify_id, send_time, read_state) VALUES
                    <foreach collection="list" item="item" index="index" separator=",">
                        (#{item.userId}, #{item.msgId}, ${@java.lang.System@currentTimeMillis()}, #{readState})
                    </foreach>
            </script>
            """)
    int insert(List<SysMessageSend> list);

    default int insert(SysMessageSend notifySend) {
        return this.insert(Collections.singletonList(notifySend));
    }
}
