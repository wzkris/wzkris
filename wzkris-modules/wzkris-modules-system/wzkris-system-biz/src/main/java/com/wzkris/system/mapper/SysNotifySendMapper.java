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
                INSERT INTO biz_sys.sys_notify_send(notify_id, user_id, read_state) VALUES
                    <foreach collection="list" item="item" index="index" separator=",">
                        (#{item.notifyId},  #{item.userId},  #{item.readState})
                    </foreach>
            </script>
            """)
    int insert(List<SysNotifySend> list);

    default int insert(SysNotifySend notifySend) {
        return this.insert(Collections.singletonList(notifySend));
    }
}
