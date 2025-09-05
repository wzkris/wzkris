package com.wzkris.user.feign.domain.resp;

import com.wzkris.user.domain.UserInfoDO;
import com.wzkris.user.domain.UserInfoDOToUserInfoManageVOMapper;
import com.wzkris.user.domain.UserInfoDOToUserInfoRespMapper;
import com.wzkris.user.domain.UserInfoDOToUserManageReqMapper;
import com.wzkris.user.domain.req.UserManageReqToUserInfoDOMapper;
import com.wzkris.user.domain.vo.UserInfoManageVOToUserInfoDOMapper;
import com.wzkris.user.feign.userinfo.resp.UserInfoResp;
import io.github.linpeilie.AutoMapperConfig__169;
import io.github.linpeilie.BaseMapper;
import org.mapstruct.Mapper;

@Mapper(
    config = AutoMapperConfig__169.class,
    uses = {UserInfoManageVOToUserInfoDOMapper.class,UserManageReqToUserInfoDOMapper.class,UserInfoDOToUserInfoManageVOMapper.class,UserInfoDOToUserManageReqMapper.class,UserInfoDOToUserInfoRespMapper.class},
    imports = {}
)
public interface UserInfoRespToUserInfoDOMapper extends BaseMapper<UserInfoResp, UserInfoDO> {
}
