package com.wzkris.user.feign.userinfo;

import com.wzkris.common.web.utils.BeanUtil;
import com.wzkris.user.domain.UserInfoDO;
import com.wzkris.user.feign.userinfo.req.LoginInfoReq;
import com.wzkris.user.feign.userinfo.req.QueryUserPermsReq;
import com.wzkris.user.feign.userinfo.resp.UserPermissionResp;
import com.wzkris.user.feign.userinfo.resp.UserInfoResp;
import com.wzkris.user.mapper.UserInfoMapper;
import com.wzkris.user.service.PermissionService;
import io.swagger.v3.oas.annotations.Hidden;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Hidden
@RestController
@RequestMapping("/feign-userinfo")
@RequiredArgsConstructor
public class UserInfoFeignImpl implements UserInfoFeign {

    private final UserInfoMapper userInfoMapper;

    private final PermissionService permissionService;

    @Override
    public UserInfoResp getByUsername(String username) {
        UserInfoDO user = userInfoMapper.selectByUsername(username);
        UserInfoResp userResp = BeanUtil.convert(user, UserInfoResp.class);
        return userResp;
    }

    @Override
    public UserInfoResp getByPhoneNumber(String phoneNumber) {
        UserInfoDO userInfoDO = userInfoMapper.selectByPhoneNumber(phoneNumber);
        UserInfoResp userResp = BeanUtil.convert(userInfoDO, UserInfoResp.class);
        return userResp;
    }

    @Override
    public UserPermissionResp getPermission(QueryUserPermsReq queryUserPermsReq) {
        return permissionService.getUserPermission(
                queryUserPermsReq.getUserId(), queryUserPermsReq.getDeptId());
    }

    @Override
    public void updateLoginInfo(LoginInfoReq loginInfoReq) {
        UserInfoDO userInfoDO = new UserInfoDO(loginInfoReq.getUserId());
        userInfoDO.setLoginIp(loginInfoReq.getLoginIp());
        userInfoDO.setLoginDate(loginInfoReq.getLoginDate());

        userInfoMapper.updateById(userInfoDO);
    }

}
