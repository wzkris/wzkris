package com.wzkris.system.mapper;

import com.wzkris.common.orm.plus.BaseMapperPlus;
import com.wzkris.system.domain.UserChatMessageDO;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface UserChatMessageMapper extends BaseMapperPlus<UserChatMessageDO> {

}
