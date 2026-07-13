package com.wzkris.usercenter.mapper;

import com.wzkris.common.orm.plus.BaseMapperPlus;
import com.wzkris.usercenter.domain.AdminLoginLogDO;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * (user_login_log)表数据库访问层
 *
 * @author wzkris
 * @since 2023-08-26 14:40:28
 */
@Mapper
@Repository
public interface AdminLoginLogMapper extends BaseMapperPlus<AdminLoginLogDO> {

}
