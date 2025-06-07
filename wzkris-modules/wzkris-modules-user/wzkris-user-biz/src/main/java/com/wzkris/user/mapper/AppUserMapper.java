package com.wzkris.user.mapper;

import com.wzkris.common.orm.plus.BaseMapperPlus;
import com.wzkris.user.domain.AppUser;
import com.wzkris.user.domain.req.AppUserIncryQueryReq;
import com.wzkris.user.domain.vo.AppUserIncryVO;
import java.util.List;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

@Repository
public interface AppUserMapper extends BaseMapperPlus<AppUser> {

    @Select("SELECT * FROM biz.app_user WHERE phone_number = #{phoneNumber}")
    AppUser selectByPhoneNumber(String phoneNumber);

    List<AppUserIncryVO> listIncryVO(AppUserIncryQueryReq queryReq);
}
