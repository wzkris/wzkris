package com.wzkris.user.mapper;

import com.wzkris.common.orm.plus.BaseMapperPlus;
import com.wzkris.user.domain.AppUserThirdinfo;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

@Repository
public interface AppUserThirdinfoMapper extends BaseMapperPlus<AppUserThirdinfo> {

    @Select("SELECT * FROM biz.app_user_thirdinfo WHERE identifier = #{identifier}")
    AppUserThirdinfo selectByIdentifier(String identifier);
}
