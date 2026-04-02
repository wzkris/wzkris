package com.wzkris.system.mapper;

import com.wzkris.common.orm.plus.BaseMapperPlus;
import com.wzkris.system.domain.TenantOperateLogDO;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface TenantOperateLogMapper extends BaseMapperPlus<TenantOperateLogDO> {

}

