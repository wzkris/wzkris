package com.thingslink.system.mapper;

import com.thingslink.common.orm.plus.BaseMapperPlus;
import com.thingslink.system.domain.SysLoginLog;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

/**
 * (LoginLog)表数据库访问层
 *
 * @author wzkris
 * @since 2023-08-26 14:40:28
 */
@Repository
public interface SysLoginLogMapper extends BaseMapperPlus<SysLoginLog> {

    @Update("truncate sys_login_log")
    void clearAll();
}
