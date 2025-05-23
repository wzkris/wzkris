package com.wzkris.user.controller;

import com.wzkris.common.core.domain.Result;
import com.wzkris.common.security.utils.ClientUtil;
import com.wzkris.common.web.model.BaseController;
import com.wzkris.user.domain.AppUser;
import com.wzkris.user.domain.req.EditAppUserProfileReq;
import com.wzkris.user.domain.vo.AppUserAccountVO;
import com.wzkris.user.mapper.AppUserMapper;
import com.wzkris.user.service.AppUserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
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
@RequestMapping("/app_user/profile")
@RequiredArgsConstructor
public class AppUserProfileController extends BaseController {

    private final static String ACCOUNT_PREFIX = "app:account";

    private final AppUserMapper appUserMapper;

    private final AppUserService appUserService;

    @Operation(summary = "账户信息")
    @GetMapping
    @Cacheable(cacheNames = ACCOUNT_PREFIX + "#1800#600", key = "@cl.getUserId()")
    public Result<AppUserAccountVO> accountVO() {
        AppUser user = appUserMapper.selectById(ClientUtil.getUserId());

        AppUserAccountVO accountVO = new AppUserAccountVO();
        AppUserAccountVO.UserInfo userInfo = new AppUserAccountVO.UserInfo();
        userInfo.setNickname(user.getNickname());
        userInfo.setPhoneNumber(user.getPhoneNumber());
        userInfo.setGender(user.getGender());
        userInfo.setAvatar(user.getAvatar());

        accountVO.setUser(userInfo);
        return ok(accountVO);
    }

    @Operation(summary = "修改昵称、性别")
    @PostMapping
    @CacheEvict(cacheNames = ACCOUNT_PREFIX, key = "@cl.getUserId()")
    public Result<?> editInfo(@RequestBody EditAppUserProfileReq profileReq) {
        AppUser user = new AppUser(ClientUtil.getUserId());
        user.setNickname(profileReq.getNickname());
        user.setGender(profileReq.getGender());
        return toRes(appUserMapper.updateById(user));
    }

    @Operation(summary = "更新头像")
    @PostMapping("/edit_avatar")
    @CacheEvict(cacheNames = ACCOUNT_PREFIX, key = "@cl.getUserId()")
    public Result<?> updateAvatar(@RequestBody String url) {
        AppUser appUser = new AppUser(ClientUtil.getUserId());
        appUser.setAvatar(url);
        return toRes(appUserMapper.updateById(appUser));
    }
}
