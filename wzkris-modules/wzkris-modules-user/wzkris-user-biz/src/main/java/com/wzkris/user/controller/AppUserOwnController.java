package com.wzkris.user.controller;

import com.wzkris.common.core.domain.Result;
import com.wzkris.common.security.utils.AppUtil;
import com.wzkris.common.web.model.BaseController;
import com.wzkris.user.api.domain.dto.AppUserDTO;
import com.wzkris.user.domain.AppUser;
import com.wzkris.user.domain.vo.AppUserAccountVO;
import com.wzkris.user.mapper.AppUserMapper;
import com.wzkris.user.service.AppUserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotBlank;
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
@RequestMapping("/app/account")
@RequiredArgsConstructor
public class AppUserOwnController extends BaseController {

    private final AppUserMapper appUserMapper;
    private final AppUserService appUserService;

    @Operation(summary = "新用户注册")
    @PostMapping("register")
    public Result<Void> register(@NotBlank(message = "[xcxCode] {validate.notnull}") String xcxCode) {
        appUserService.registerByXcx(xcxCode);
        return ok();
    }

    @Operation(summary = "账户信息")
    @GetMapping
    public Result<AppUserAccountVO> accountVO() {
        AppUser appUser = appUserMapper.selectById(AppUtil.getUserId());

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
    @PostMapping
    public Result<?> editInfo(@RequestBody AppUserDTO appUserDTO) {
        AppUser user = new AppUser(AppUtil.getUserId());
        user.setNickname(appUserDTO.getNickname());
        user.setGender(appUserDTO.getGender());
        return toRes(appUserMapper.updateById(user));
    }

    @Operation(summary = "更新头像")
    @PostMapping("/edit_avatar")
    public Result<?> updateAvatar(@RequestBody String url) {
        AppUser appUser = new AppUser(AppUtil.getUserId());
        appUser.setAvatar(url);
        return toRes(appUserMapper.updateById(appUser));
    }
}
