package com.thingslink.system.mapper;

import com.thingslink.common.orm.plus.BaseMapperPlus;
import com.thingslink.system.domain.OperLog;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

/**
 * 操作日志 数据层
 *
 * @author wzkris
 */
@Repository
public interface OperLogMapper extends BaseMapperPlus<OperLog> {

    /**
     * 清空操作日志
     */
    @Update("truncate TABLE oper_log")
    void clearAll();
}
