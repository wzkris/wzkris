package com.wzkris.message.mapper;

import com.wzkris.common.orm.plus.BaseMapperPlus;
import com.wzkris.message.domain.UserOperateLogDO;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * 操作日志 数据层
 *
 * @author wzkris
 */
@Mapper
@Repository
public interface UserOperateLogMapper extends BaseMapperPlus<UserOperateLogDO> {

}
