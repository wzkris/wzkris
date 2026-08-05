package com.wzkris.usercenter.mapper;

import com.wzkris.common.orm.plus.BaseMapperPlus;
import com.wzkris.usercenter.domain.MemberToPostDO;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface MemberToPostMapper extends BaseMapperPlus<MemberToPostDO> {

}
