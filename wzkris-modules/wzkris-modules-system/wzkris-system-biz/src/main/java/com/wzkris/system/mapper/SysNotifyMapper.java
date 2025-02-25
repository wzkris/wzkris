package com.wzkris.system.mapper;

import com.wzkris.common.orm.plus.BaseMapperPlus;
import com.wzkris.system.domain.SysNotify;
import com.wzkris.system.domain.vo.SysNotifyVO;
import jakarta.annotation.Nullable;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SysNotifyMapper extends BaseMapperPlus<SysNotify> {

    @Select("""
            <script>
                SELECT * FROM sys_notify_send s LEFT JOIN sys_notify n ON s.notify_id = n.notify_id
                WHERE user_id = #{userId}
            	    <if test="notifyType != null and notifyType != ''">
            	        AND notify_type = #{notifyType}
            	    </if>
            	    <if test="readState != null and readState != ''">
            	        AND read_state = #{readState}
            	    </if>
                ORDER BY s.notify_id DESC
            </script>
            """)
    List<SysNotifyVO> listNotify(@Param("userId") Long userId, @Nullable @Param("notifyType") String notifyType, @Nullable @Param("readState") String readState);

    /**
     * 已读通知
     */
    @Update("UPDATE sys_notify_send SET read_state = '1' WHERE notify_id = #{notifyId} AND user_id = #{userId}")
    int markRead(@Param("notifyId") Long notifyId, @Param("userId") Long userId);

    /**
     * 最大统计100
     */
    @Select("""
            <script>
                SELECT COUNT(*) FROM sys_notify_send s LEFT JOIN sys_notify n ON s.notify_id = n.notify_id
                WHERE user_id = #{userId} AND read_state = '0'
                    <if test="notifyType != null and notifyType != ''">
            	        AND notify_type = #{notifyType}
            	    </if>
                ORDER BY s.notify_id DESC LIMIT 100
            </script>
            """)
    int countUnread(@Param("userId") Long userId, @Nullable @Param("notifyType") String notifyType);

}
