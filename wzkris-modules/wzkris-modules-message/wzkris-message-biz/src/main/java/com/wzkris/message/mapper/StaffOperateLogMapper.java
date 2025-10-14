package com.wzkris.message.mapper;

import com.wzkris.common.orm.plus.BaseMapperPlus;
import com.wzkris.message.domain.StaffOperateLogDO;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface StaffOperateLogMapper extends BaseMapperPlus<StaffOperateLogDO> {

}
