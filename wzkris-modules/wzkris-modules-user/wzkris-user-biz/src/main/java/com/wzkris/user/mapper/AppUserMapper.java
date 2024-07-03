package com.wzkris.user.mapper;

import com.wzkris.common.orm.plus.BaseMapperPlus;
import com.wzkris.user.domain.AppUser;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

@Repository
public interface AppUserMapper extends BaseMapperPlus<AppUser> {

    @Select("SELECT * FROM app_user WHERE phone_number = #{phoneNumber}")
    AppUser selectByPhoneNumber(String phoneNumber);
}
