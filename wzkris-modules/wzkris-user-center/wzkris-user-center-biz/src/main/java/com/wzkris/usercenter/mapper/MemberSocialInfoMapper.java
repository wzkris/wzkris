package com.wzkris.usercenter.mapper;

import com.wzkris.common.orm.plus.BaseMapperPlus;
import com.wzkris.usercenter.domain.MemberSocialInfoDO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface MemberSocialInfoMapper extends BaseMapperPlus<MemberSocialInfoDO> {

    @Select("SELECT * FROM biz.member_social_info WHERE identifier = #{identifier}")
    MemberSocialInfoDO selectByIdentifier(String identifier);

}
