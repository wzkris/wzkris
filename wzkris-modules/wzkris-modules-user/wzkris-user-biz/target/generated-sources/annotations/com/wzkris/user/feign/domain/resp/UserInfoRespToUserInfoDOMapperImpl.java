package com.wzkris.user.feign.domain.resp;

import com.wzkris.user.domain.UserInfoDO;
import javax.annotation.processing.Generated;

import com.wzkris.user.feign.userinfo.resp.UserInfoResp;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-09-05T17:10:48+0800",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 17.0.16 (Oracle Corporation)"
)
@Component
public class UserInfoRespToUserInfoDOMapperImpl implements UserInfoRespToUserInfoDOMapper {

    @Override
    public UserInfoDO convert(UserInfoResp arg0) {
        if ( arg0 == null ) {
            return null;
        }

        UserInfoDO userInfoDO = new UserInfoDO();

        userInfoDO.setUserId( arg0.getUserId() );
        userInfoDO.setTenantId( arg0.getTenantId() );
        userInfoDO.setDeptId( arg0.getDeptId() );
        userInfoDO.setUsername( arg0.getUsername() );
        userInfoDO.setNickname( arg0.getNickname() );
        userInfoDO.setEmail( arg0.getEmail() );
        userInfoDO.setPhoneNumber( arg0.getPhoneNumber() );
        userInfoDO.setStatus( arg0.getStatus() );
        userInfoDO.setPassword( arg0.getPassword() );

        return userInfoDO;
    }

    @Override
    public UserInfoDO convert(UserInfoResp arg0, UserInfoDO arg1) {
        if ( arg0 == null ) {
            return arg1;
        }

        arg1.setUserId( arg0.getUserId() );
        arg1.setTenantId( arg0.getTenantId() );
        arg1.setDeptId( arg0.getDeptId() );
        arg1.setUsername( arg0.getUsername() );
        arg1.setNickname( arg0.getNickname() );
        arg1.setEmail( arg0.getEmail() );
        arg1.setPhoneNumber( arg0.getPhoneNumber() );
        arg1.setStatus( arg0.getStatus() );
        arg1.setPassword( arg0.getPassword() );

        return arg1;
    }
}
