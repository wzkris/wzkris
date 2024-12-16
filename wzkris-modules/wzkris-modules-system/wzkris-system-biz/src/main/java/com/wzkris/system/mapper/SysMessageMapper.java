package com.wzkris.system.mapper;

import com.wzkris.common.orm.plus.BaseMapperPlus;
import com.wzkris.system.domain.SysMessage;
import com.wzkris.system.domain.vo.SysNotifyVO;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 系统消息表 数据层
 *
 * @author wzkris
 */
@Repository
public interface SysMessageMapper extends BaseMapperPlus<SysMessage> {

    @Select("""
            SELECT m.*, s.read_state FROM sys_message m RIGHT JOIN sys_notify_send s ON m.msg_id = s.msg_id
            WHERE msg_type = '1' AND status = '2' AND user_id = #{userId} ORDER BY send_time DESC
            """)
    List<SysNotifyVO> listNotify(Long userId);
}
