package com.thingslink.user.mapper;

import com.thingslink.common.orm.plus.BaseMapperPlus;
import com.thingslink.user.domain.AppUser;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

@Repository
public interface AppUserMapper extends BaseMapperPlus<AppUser> {

    @Select("SELECT * FROM app_user WHERE phone_number = #{phoneNumber}")
    AppUser selectByPhoneNumber(String phoneNumber);
}
