package com.wzkris.usercenter.mapper;

import com.wzkris.common.orm.plus.BaseMapperPlus;
import com.wzkris.usercenter.domain.CustomerLoginLogDO;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * 用户登录日志 数据层
 *
 * @author wzkris
 */
@Mapper
@Repository
public interface CustomerLoginLogMapper extends BaseMapperPlus<CustomerLoginLogDO> {

}
