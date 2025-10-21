package com.wzkris.principal.feign.user;

import com.wzkris.common.web.utils.BeanUtil;
import com.wzkris.principal.domain.UserInfoDO;
import com.wzkris.principal.feign.user.req.LoginInfoReq;
import com.wzkris.principal.feign.user.req.QueryUserPermsReq;
import com.wzkris.principal.feign.user.resp.UserInfoResp;
import com.wzkris.principal.feign.user.resp.UserPermissionResp;
import com.wzkris.principal.mapper.UserInfoMapper;
import com.wzkris.principal.service.PermissionService;
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
