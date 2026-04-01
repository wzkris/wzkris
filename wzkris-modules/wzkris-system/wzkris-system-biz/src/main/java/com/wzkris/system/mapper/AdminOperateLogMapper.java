package com.wzkris.system.mapper;

import com.wzkris.common.orm.plus.BaseMapperPlus;
import com.wzkris.system.domain.AdminOperateLogDO;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * 操作日志 数据层
 *
 * @author wzkris
 */
@Mapper
@Repository
public interface AdminOperateLogMapper extends BaseMapperPlus<AdminOperateLogDO> {

}
