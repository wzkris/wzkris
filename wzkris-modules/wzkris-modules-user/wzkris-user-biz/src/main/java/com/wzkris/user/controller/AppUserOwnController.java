package com.wzkris.user.controller;

import com.wzkris.common.core.domain.Result;
import com.wzkris.common.core.utils.MapstructUtil;
import com.wzkris.common.security.oauth2.domain.model.ClientUser;
import com.wzkris.common.security.utils.ClientUserUtil;
import com.wzkris.common.web.model.BaseController;
import com.wzkris.user.domain.AppUser;
import com.wzkris.user.domain.req.EditOwnAppUserReq;
import com.wzkris.user.domain.vo.AppUserAccountVO;
import com.wzkris.user.domain.vo.AppUserOwnVO;
import com.wzkris.user.mapper.AppUserMapper;
import com.wzkris.user.service.AppUserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * 用户个人信息
 *
 * @author wzkris
 */
@Tag(name = "app账户")
@Slf4j
@Validated
@RestController
@RequestMapping("/app")
@RequiredArgsConstructor
public class AppUserOwnController extends BaseController {

    private final AppUserMapper appUserMapper;
    private final AppUserService appUserService;

    @Operation(summary = "登录信息")
    @GetMapping("/info")
    public Result<AppUserOwnVO> appUser() {
        ClientUser clientUser = ClientUserUtil.getClientUser();
        AppUserOwnVO appUserOwnVO = MapstructUtil.convert(clientUser, AppUserOwnVO.class);
        return ok(appUserOwnVO);
    }

    @Operation(summary = "账户信息")
    @GetMapping("/account")
    public Result<AppUserAccountVO> accountVO() {
        AppUser appUser = appUserMapper.selectById(ClientUserUtil.getUserId());

        AppUserAccountVO accountVO = new AppUserAccountVO();
        AppUserAccountVO.UserInfo userInfo = new AppUserAccountVO.UserInfo();
        userInfo.setNickname(appUser.getNickname());
        userInfo.setPhoneNumber(appUser.getPhoneNumber());
        userInfo.setGender(appUser.getGender());
        userInfo.setAvatar(appUser.getAvatar());

        accountVO.setUser(userInfo);
        return ok(accountVO);
    }

    @Operation(summary = "修改昵称、性别")
    @PostMapping("/account")
    public Result<?> editInfo(@RequestBody EditOwnAppUserReq userReq) {
        AppUser user = new AppUser(ClientUserUtil.getUserId());
        user.setNickname(userReq.getNickname());
        user.setGender(userReq.getGender());
        return toRes(appUserMapper.updateById(user));
    }

    @Operation(summary = "更新头像")
    @PostMapping("/account/edit_avatar")
    public Result<?> updateAvatar(@RequestBody String url) {
        AppUser appUser = new AppUser(ClientUserUtil.getUserId());
        appUser.setAvatar(url);
        return toRes(appUserMapper.updateById(appUser));
    }
}
