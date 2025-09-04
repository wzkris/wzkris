package com.wzkris.user.mapper;

import com.wzkris.common.orm.plus.BaseMapperPlus;
import com.wzkris.user.domain.CustomerSocialInfoDO;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerSocialInfoMapper extends BaseMapperPlus<CustomerSocialInfoDO> {

    @Select("SELECT * FROM biz.customer_social_info WHERE identifier = #{identifier}")
    CustomerSocialInfoDO selectByIdentifier(String identifier);

}
