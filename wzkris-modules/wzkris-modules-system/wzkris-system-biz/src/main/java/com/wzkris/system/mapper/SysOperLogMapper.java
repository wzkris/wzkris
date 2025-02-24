package com.wzkris.system.mapper;

import com.wzkris.common.orm.plus.BaseMapperPlus;
import com.wzkris.system.domain.SysOperLog;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

/**
 * 操作日志 数据层
 *
 * @author wzkris
 */
@Repository
public interface SysOperLogMapper extends BaseMapperPlus<SysOperLog> {

    /**
     * 清空操作日志
     */
    @Update("truncate TABLE biz_sys.sys_oper_log")
    void clearAll();
}
