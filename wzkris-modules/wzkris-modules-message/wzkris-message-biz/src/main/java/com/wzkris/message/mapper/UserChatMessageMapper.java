package com.wzkris.message.mapper;

import com.wzkris.common.orm.plus.BaseMapperPlus;
import com.wzkris.message.domain.UserChatMessageDO;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface UserChatMessageMapper extends BaseMapperPlus<UserChatMessageDO> {

}
