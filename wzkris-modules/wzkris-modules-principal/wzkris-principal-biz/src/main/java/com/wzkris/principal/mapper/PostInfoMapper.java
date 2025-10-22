package com.wzkris.principal.mapper;

import com.wzkris.common.orm.plus.BaseMapperPlus;
import com.wzkris.principal.domain.PostInfoDO;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface PostInfoMapper extends BaseMapperPlus<PostInfoDO> {

}
