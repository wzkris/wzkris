package com.wzkris.system.mapper;

import com.wzkris.common.orm.plus.BaseMapperPlus;
import com.wzkris.system.domain.SysLoginLog;
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

    @Update("truncate biz_sys.sys_login_log")
    void clearAll();
}
