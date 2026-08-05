package com.wzkris.payment.mapper;

import com.wzkris.common.orm.plus.BaseMapperPlus;
import com.wzkris.payment.domain.PayNotifyTaskDO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

import java.time.OffsetDateTime;

@Mapper
@Repository
public interface PayNotifyTaskMapper extends BaseMapperPlus<PayNotifyTaskDO> {

    /**
     * 原子认领：PENDING 或卡死的 SENDING(update_at 早于 staleBefore) -> SENDING，并刷新 update_at。
     * 返回 0 表示已被其他实例认领或已完成，防止多实例/重试Job/首次投递并发重复发送。
     */
    @Update("UPDATE biz.pay_notify_task SET status = 'SENDING', update_at = now() "
            + "WHERE task_id = #{taskId} "
            + "AND (status = 'PENDING' OR (status = 'SENDING' AND update_at < #{staleBefore}))")
    int claimSending(Long taskId, OffsetDateTime staleBefore);

}
