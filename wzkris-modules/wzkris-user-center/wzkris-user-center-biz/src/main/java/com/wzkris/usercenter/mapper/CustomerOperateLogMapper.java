package com.wzkris.usercenter.mapper;

import com.wzkris.common.orm.plus.BaseMapperPlus;
import com.wzkris.usercenter.domain.CustomerOperateLogDO;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * 用户操作日志 数据层
 *
 * @author wzkris
 */
@Mapper
@Repository
public interface CustomerOperateLogMapper extends BaseMapperPlus<CustomerOperateLogDO> {

}
