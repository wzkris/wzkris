package com.wzkris.system.mapper;

import com.wzkris.system.domain.SysNoticeUser;
import java.util.Collections;
import java.util.List;
import org.apache.ibatis.annotations.Insert;
import org.springframework.stereotype.Repository;

/**
 * 通知发送表 数据层
 *
 * @author wzkris
 */
@Repository
public interface SysNoticeUserMapper {

    @Insert(
            """
            <script>
                INSERT INTO biz.sys_notice_user(notice_id, user_id, read_state) VALUES
                    <foreach collection="list" item="item" index="index" separator=",">
                        (#{item.noticeId},  #{item.userId},  #{item.readState})
                    </foreach>
            </script>
            """)
    int insert(List<SysNoticeUser> list);

    default int insert(SysNoticeUser notifySend) {
        return this.insert(Collections.singletonList(notifySend));
    }
}
